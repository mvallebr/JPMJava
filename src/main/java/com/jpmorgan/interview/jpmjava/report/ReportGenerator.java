package com.jpmorgan.interview.jpmjava.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.jpmorgan.interview.jpmjava.dataaccess.Dataset;
import com.jpmorgan.interview.jpmjava.model.Instruction;

public class ReportGenerator {
	private final Dataset dataset;
	private final OutputStream ostream;
	private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
	private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

	public ReportGenerator(Dataset dataset, OutputStream ostream) {
		this.dataset = dataset;
		this.ostream = ostream;
	}

	private Map<String, Double> rankEntities(List<Instruction> instructions, Predicate<Instruction> predicate) {
		return instructions.stream().filter(predicate).collect(Collectors.groupingBy(Instruction::getEntity,
				Collectors.summingDouble(Instruction::calculateUsdAmount)));
	}

	private void printEntitiesByRank(PrintWriter pw, Map<String, Double> rankedEntities, String prefix) {
		pw.println(rankedEntities.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry<String, Double>::getValue).reversed()).map(Map.Entry::getKey)
				.collect(Collectors.joining(" ", prefix, "")));
	}

	private SortedMap<LocalDate, List<Instruction>> instructionsMappedByDate() {
		return StreamSupport.stream(dataset.queryAll().spliterator(), false).collect(
				Collectors.groupingBy(Instruction::calculateActualSettlementDate, TreeMap::new, Collectors.toList()));
	}

	private void printDateHeader(PrintWriter pw, String date, String incommingAmount, String outgoingAmount) {
		final String initial = "|";
		final String separator = String.format("%5s", initial);
		pw.print(initial);
		pw.print(String.format("%20s", date));
		pw.print(separator);
		pw.print(String.format("%20s", incommingAmount));
		pw.print(separator);
		pw.print(String.format("%20s", outgoingAmount));
		pw.println(separator);
	}

	private void printDateDetail(PrintWriter pw, LocalDate date, Double incommingAmount, Double outgoingAmount) {
		printDateHeader(pw, dtFormatter.format(date), currencyFormatter.format(incommingAmount),
				currencyFormatter.format(outgoingAmount));
	}

	public void generate() throws IOException {
		try (PrintWriter pw = new PrintWriter(ostream);) {
			printDateHeader(pw, "Settlement Date", "Incoming", "Outgoing");

			SortedMap<LocalDate, List<Instruction>> instructionsByDate = instructionsMappedByDate();

			instructionsByDate.forEach((date, instructions) -> {
				Map<String, Double> incommingRankByEntity = rankEntities(instructions, Instruction::getSell);
				Double incommingAmount = incommingRankByEntity.values().stream().reduce(0d, (x, y) -> x + y);
				
				Map<String, Double> outgoingRankByEntity = rankEntities(instructions, Instruction::getBuy);
				Double outgoingAmount = outgoingRankByEntity.values().stream().reduce(0d, (x, y) -> x + y);

				printDateDetail(pw, date, incommingAmount, outgoingAmount);
				printEntitiesByRank(pw, incommingRankByEntity, "incoming rank: ");
				printEntitiesByRank(pw, outgoingRankByEntity, "outgoing rank: ");
			});

		}
	}
}
