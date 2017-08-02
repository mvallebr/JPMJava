package com.jpmorgan.interview.jpmjava.dataaccess;

import com.jpmorgan.interview.jpmjava.model.Instruction;

public interface Dataset {
	public Iterable<Instruction> queryAll();
}
