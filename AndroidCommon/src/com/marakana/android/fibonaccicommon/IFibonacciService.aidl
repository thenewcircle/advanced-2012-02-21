package com.marakana.android.fibonaccicommon;

import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;

interface IFibonacciService {
	long fibJR(long n);
	long fibJI(long n);
	long fibNR(long n);
	long fibNI(long n);
	FibonacciResponse fib(in FibonacciRequest request);
}