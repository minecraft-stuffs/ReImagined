package net.rhseung.reimagined.tool

import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.rhseung.reimagined.tool.parts.enums.PartType
import net.rhseung.reimagined.utils.Color
import net.rhseung.reimagined.utils.Math.pow
import net.rhseung.reimagined.utils.Math.roundTo
import kotlin.math.max
import kotlin.math.min

enum class Material(
	var canParts: Set<PartType>,
	val ingredient: ItemConvertible? = null,
	val color: Color = Color.WHITE,
	var tier: Int = -1,
	var weight: Int = -1,
	var hardness: Int = -1,
	
	val trait: Trait? = null,
	val isMetal: Boolean = true,
	/** for not vanilla materials */
	val worldSpawn: Boolean = false,
) {
	DUMMY(
		setOf(
			*PartType.HEAD,
			PartType.BINDING,
			PartType.HANDLE
		)
	),
	
	@Property(tier = 0, weight = 0, hardness = 1)
	STRING(
		setOf(
			PartType.BINDING
		), Items.STRING, Color.STRING
	),
	
	@Property(tier = 0, weight = 0, hardness = 1)
	VINE(
		setOf(
			PartType.BINDING
		), Items.VINE, Color.VINE
	),
	
	@Property(tier = 0, weight = 1, hardness = 3)
	WOOD(
		setOf(
			*PartType.HEAD,
			PartType.BINDING,
			PartType.HANDLE
		), Items.OAK_PLANKS, Color.WOOD
	),
	
	@Property(tier = 1, weight = 2, hardness = 2)
	STONE(
		setOf(
			*PartType.HEAD,
			PartType.HANDLE
		), Items.COBBLESTONE, Color.STONE
	),
	
	@Property(tier = 2, weight = 5, hardness = 8)
	COPPER(
		setOf(
			*PartType.HEAD,
			PartType.BINDING,
			PartType.HANDLE
		), Items.COPPER_INGOT, Color.COPPER
	),
	
	@Property(tier = 3, weight = 4, hardness = 20)
	IRON(
		setOf(
			*PartType.HEAD,
			PartType.BINDING,
			PartType.HANDLE
		), Items.IRON_INGOT, Color.IRON
	),
	
	@Property(tier = 4, weight = 2, hardness = 60)
	DIAMOND(
		setOf(
			*PartType.HEAD,
			PartType.BINDING,
			PartType.HANDLE
		), Items.DIAMOND, Color.DIAMOND
	),
	// todofar: 다이아몬드는 보석류이므로 나중에 없앨거임
	//      - 강철로 할 것 같음
	//      - 용광로(->합금제조기)랑 훈연기(->코크오븐) 오버라이딩해서 만들 예정
	
	@Property(tier = 5, weight = 4, hardness = 75)
	NETHERITE(
		setOf(
			*PartType.HEAD,
			PartType.BINDING,
			PartType.HANDLE
		), Items.NETHERITE_INGOT, Color.NETHERITE
	);
	
	init {
		this.declaringJavaClass.getField(name).annotations.forEach { annotation ->
			when (annotation) {
				is Property -> {
					tier = annotation.tier
					weight = annotation.weight
					hardness = annotation.hardness
				}
			}
		}
	}
	
	fun getStat(s: Stat): Float {
		return if (this == DUMMY) s.defaultValue
		else when (s) {
			Stat.DURABILITY -> max((-15.4079
					+ 139.596*tier - 52.6629*tier.pow(2)
					- 1.42673*weight - 0.183615 * weight.pow(2)
					+ 12.7571*hardness + 0.30522*hardness.pow(2)
			).roundTo(0), 10.0)
			
			Stat.ATTACK_DAMAGE -> 1.3*tier + 0.4*weight
			
			Stat.ATTACK_SPEED -> 0.1*(2 - weight)
			
			Stat.MINING_TIER -> tier
			
			Stat.MINING_SPEED -> 3 + 3*tier - 0.3*weight
			
			Stat.ENCHANTABILITY -> min(7 - 0.5*tier + 2.5*(4 - weight), 25.0)
		}.toFloat()
	}
	
	companion object {
		fun get(miningLevel: Int) = when (miningLevel) {
			-1 -> DUMMY
			0 -> WOOD
			1 -> STONE
			2 -> COPPER
			3 -> IRON
			4 -> DIAMOND
			5 -> NETHERITE
			else -> error("Invalid mining level: $miningLevel")
		}
		
		fun getColor(miningLevel: Int): Color {
			return get(miningLevel).color
		}
		
		fun getValues() = Material.values().filter { it != DUMMY }
	}
}