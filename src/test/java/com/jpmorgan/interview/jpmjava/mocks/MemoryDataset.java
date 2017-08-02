package com.jpmorgan.interview.jpmjava.mocks;

import java.util.List;

import com.jpmorgan.interview.jpmjava.dataaccess.Dataset;
import com.jpmorgan.interview.jpmjava.model.Instruction;

/**
 * Dataset implementation that receives a set of instructions and hold them in
 * memory Real implementation could query data from a database, file or nosql
 * store, for instance.
 *
 */
public class MemoryDataset implements Dataset {

	private List<Instruction> memory;

	public MemoryDataset(List<Instruction> memory) {
		super();
		this.memory = memory;
	}

	@Override
	public Iterable<Instruction> queryAll() {

		return memory;
	}

}
