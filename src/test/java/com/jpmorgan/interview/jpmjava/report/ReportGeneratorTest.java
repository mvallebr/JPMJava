package com.jpmorgan.interview.jpmjava.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import com.jpmorgan.interview.jpmjava.mocks.MemoryDataset;
import com.jpmorgan.interview.jpmjava.model.Instruction;

public class ReportGeneratorTest {
	/**
	 * This compares the contents written to the byte array output stream (actual)
	 * with the contents of file (expected)
	 * 
	 */
	private void checkResult(List<Instruction> input, String filename) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ReportGenerator rg = new ReportGenerator(new MemoryDataset(input), baos);
		rg.generate();
		try (Scanner expectedScanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream(filename),
				"utf-8")) {
			String expected = expectedScanner.useDelimiter("\\A").next(); // Reads the whole file
			String actual = baos.toString("UTF-8");
			Assert.assertEquals(expected, actual);
		}
	}

	private void generateOnSysout(List<Instruction> input) throws IOException {
		ReportGenerator rg = new ReportGenerator(new MemoryDataset(input), System.out);
		rg.generate();
	}

	@Test
	public void testGenerateWorksWithStdout() throws IOException {
		List<Instruction> input = new ArrayList<>();
		input.add(new Instruction("foo", Instruction.BUY, new BigDecimal(0.50d), "SGP", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 2), 200, new BigDecimal(100.25d)));
		generateOnSysout(input);
	}

	@Test
	public void testNoneSpecialWeekdays() throws IOException {
		List<Instruction> input = new ArrayList<>();
		input.add(new Instruction("foo", Instruction.BUY, new BigDecimal(0.50d), "SGP", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 5), 200, new BigDecimal(100.25d)));
		input.add(new Instruction("bar", Instruction.SELL, new BigDecimal(0.22d), "GBP", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 450, new BigDecimal(150.5d)));
		input.add(new Instruction("foo", Instruction.SELL, new BigDecimal(0.33d), "BRL", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 154, new BigDecimal(1000.5d)));

		checkResult(input, "none_special_weekdays.out");
	}

	@Test
	public void testSpecialWeekdays() throws IOException {
		List<Instruction> input = new ArrayList<>();
		input.add(new Instruction("foo", Instruction.BUY, new BigDecimal(0.50d), "SAR", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 5), 200, new BigDecimal(100.25d)));
		input.add(new Instruction("bar", Instruction.SELL, new BigDecimal(0.22d), "AED", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 450, new BigDecimal(150.5d)));
		input.add(new Instruction("foo", Instruction.SELL, new BigDecimal(0.33d), "SAR", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 154, new BigDecimal(1000.5d)));

		checkResult(input, "special_weekdays.out");
	}

	@Test
	public void testNoInputs() throws IOException {
		List<Instruction> input = new ArrayList<>();

		checkResult(input, "no_inputs.out");
	}

	@Test
	public void testWeekendAED() throws IOException {
		List<Instruction> input = new ArrayList<>();
		input.add(new Instruction("bar", Instruction.SELL, new BigDecimal(0.22d), "AED", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 2), 450, new BigDecimal(150.5d)));

		checkResult(input, "weekend_AED.out");
	}

	@Test
	public void testWeekendSAR() throws IOException {
		List<Instruction> input = new ArrayList<>();
		input.add(new Instruction("bar", Instruction.SELL, new BigDecimal(0.22d), "SAR", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 2), 450, new BigDecimal(150.5d)));
		input.add(new Instruction("bar", Instruction.SELL, new BigDecimal(0.22d), "SAR", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 1), 450, new BigDecimal(150.5d)));

		checkResult(input, "weekend_SAR.out");
	}

	@Test
	public void testNoneSpecialWeekend() throws IOException {
		List<Instruction> input = new ArrayList<>();
		input.add(new Instruction("bar", Instruction.SELL, new BigDecimal(0.22d), "GBP", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 2), 450, new BigDecimal(150.5d)));
		input.add(new Instruction("bar", Instruction.SELL, new BigDecimal(0.22d), "BRL", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 3), 450, new BigDecimal(150.5d)));

		checkResult(input, "none_special_weekend.out");
	}

	@Test
	public void testComplexRanking() throws IOException {
		List<Instruction> input = new ArrayList<>();
		input.add(new Instruction("foo", Instruction.BUY, new BigDecimal(10d), "SAR", LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 1, 5), 10, new BigDecimal(10d)));
		input.add(new Instruction("bar", Instruction.BUY, new BigDecimal(3d), "SAR", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 3, new BigDecimal(3d)));
		input.add(new Instruction("A", Instruction.SELL, new BigDecimal(1d), "AED", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 1, new BigDecimal(1d)));
		input.add(new Instruction("B", Instruction.SELL, new BigDecimal(2d), "SAR", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 2, new BigDecimal(2d)));
		input.add(new Instruction("C", Instruction.SELL, new BigDecimal(2d), "GBP", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 3, new BigDecimal(2d)));
		input.add(new Instruction("D", Instruction.SELL, new BigDecimal(2d), "BRL", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 4, new BigDecimal(2d)));
		input.add(new Instruction("E", Instruction.SELL, new BigDecimal(2d), "TKY", LocalDate.of(2016, 1, 5),
				LocalDate.of(2016, 1, 7), 5, new BigDecimal(2d)));
		checkResult(input, "complex_ranking.out");
	}

}
