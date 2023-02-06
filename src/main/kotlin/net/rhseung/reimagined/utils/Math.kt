package net.rhseung.reimagined.utils

object Math {
	fun sum(vararg numbers: Int): Int {
		return numbers.reduce { acc, cur -> acc + cur }
	}
	
	fun average(vararg numbers: Int): Float {
		return sum(*numbers).toFloat() / numbers.count()
	}
	
	fun sum(vararg numbers: Float): Float {
		return numbers.reduce { acc, cur -> acc + cur }
	}
	
	fun average(vararg numbers: Float): Float {
		return sum(*numbers) / numbers.count()
	}
}