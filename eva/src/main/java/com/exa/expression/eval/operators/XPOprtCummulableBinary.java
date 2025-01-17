package com.exa.expression.eval.operators;

import java.util.Vector;

import com.exa.eva.ComputedItem;
import com.exa.eva.EvaException;
import com.exa.expression.OM;
import com.exa.expression.XPOperand;
import com.exa.expression.XPOperatorBase;
import com.exa.expression.XPression;
import com.exa.expression.eval.XPEvaluator;
import com.exa.utils.ManagedException;

public abstract class XPOprtCummulableBinary<T> extends XPOperatorBase<T> {

	public XPOprtCummulableBinary(String symbol) {
		super(symbol, 2);
	}

	@Override
	public void resolve(XPEvaluator eval, int order, int nbOperands) throws ManagedException {
		if(eval.numberOfOperands() < nbOperands) throw new EvaException(String.format("Error in the expression near %s . The number of argument expected %s is is lower than the availabe %s", symbol, nbOperands, eval.numberOfOperands()));
		
		Vector<XPOperand<?>> params = new Vector<>();
		
		for(int i=0; i < nbOperands; i++) {
			ComputedItem<XPression<?>, XPression<?>, OM> ci = eval.popOperand();
			XPression<?> item = ci.item();
			XPOperand<?> oprd = item.asOperand();
			if(oprd == null) {
				item.asOperator().resolve(eval, ci.order(), ci.asComputedOperator().nbOperands());
				oprd = eval.popOperand().item().asOperand();
			}
			
			params.insertElementAt(oprd, 0);
		}
		
				
		XPOperand<T> res = createResultOperand(params);
		
		eval.push(res);
	}
	
	public abstract XPOperand<T> createResultOperand(Vector<XPOperand<?>> params) throws ManagedException;
	
	

	@Override
	public boolean canManage(XPEvaluator eval, int order, int nbOperands) throws ManagedException {
		if(eval.numberOfOperands() < nbOperands) return false;
		
		return true;
	}


}
