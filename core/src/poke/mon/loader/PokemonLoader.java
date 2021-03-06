package poke.mon.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import poke.mon.pokemon.PokemonAbilities;
import poke.mon.pokemon.PokemonCreator;
import poke.mon.pokemon.PokemonStats;
import poke.mon.pokemon.PokemonTypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class PokemonLoader {
	/*TODO
	 * Parse moves and eggMoves
	 * parse items :
		<uncommon />
		<common />
		<rare />
		
		<evolution name="LOMBRE" condition="Level" value="14"/>
		<evolution name="VAPOREON" condition="Item" value="WATERSTONE"/>
		<evolution name="JOLTEON" condition="Item" value="THUNDERSTONE"/>
		<evolution name="FLAREON" condition="Item" value="FIRESTONE"/>
		<evolution name="LEAFEON" condition="Location" value="28"/>
		<evolution name="GLACEON" condition="Location" value="34"/>
		<evolution name="ESPEON" condition="HappinessDay" value=""/>
		<evolution name="UMBREON" condition="HappinessNight"/>
		
	 * */
	private static ArrayList<PokemonCreator> pokedex = new ArrayList<PokemonCreator>();
	private XmlReader reader = new XmlReader();
	private Element element;
	private Array<Element> elements;
	private String values;
	private String[] modify;
	
	public PokemonLoader() {
		try {
			element = reader.parse(Gdx.files.internal("data/pokemon.xml"));
			elements = element.getChildrenByName("pokemon");
			
			for (Element pokemons : elements) {
				PokemonCreator pokemon = new PokemonCreator();
				pokemon.setId(Integer.parseInt(pokemons.getAttribute("id")));
				pokemon.setName(pokemons.getAttribute("name"));
				values = pokemons.getChildByName("types").getAttribute("value");
				if (values != null) {
					modify = values.split(",");
					PokemonTypes[] types = new PokemonTypes[modify.length];
					for (int i = 0; i < modify.length; i++) {
						types[i] = PokemonTypes.UNKOWN.getTypeByString(modify[i]);
					}
					pokemon.setTypes(types);
				}
				if (pokemons.getChildByName("stats").getAttribute("value") != null) {
					values = pokemons.getChildByName("stats").getAttribute("value");
					modify = values.split(",");
					for (int i = 0; i < modify.length; i++) {
						pokemon.getEv().put(PokemonStats.UNKNOWN.getStat(i), Integer.parseInt(modify[i]));
					}
				}
				if (pokemons.getChildByName("gender") != null) {
					pokemon.setGenderRate(pokemons.getChildByName("gender").getAttribute("value"));
				}
				if (pokemons.getChildByName("growthrate").getAttribute("value") != null) {
					pokemon.setGrowthRate(pokemons.getChildByName("growthrate").getAttribute("value"));
				}	
				if (pokemons.getChildByName("experience").getAttribute("value") != null) {
					pokemon.setBaseExp(Integer.parseInt(pokemons.getChildByName("experience").getAttribute("value")));
				}	
				if (pokemons.getChildByName("effortpoints").getAttribute("value") != null) {
					values = pokemons.getChildByName("effortpoints").getAttribute("value");
					modify = values.split(",");
					for (int i = 0; i < modify.length; i++) {
						pokemon.getEffortPoints().put(PokemonStats.UNKNOWN.getStat(i), Integer.parseInt(modify[i]));
					}
				}
				if (pokemons.getChildByName("rareness").getAttribute("value") != null) {
					pokemon.setRareness(Integer.parseInt(pokemons.getChildByName("rareness").getAttribute("value")));
				}
				if (pokemons.getChildByName("happiness").getAttribute("value") != null) {
					pokemon.setHappiness(Integer.parseInt(pokemons.getChildByName("happiness").getAttribute("value")));
				}
				if (pokemons.getChildrenByName("abilities") != null) {
					Array<Element> elemen = pokemons.getChildrenByName("abilities");
					PokemonAbilities[] abilList = new PokemonAbilities[elemen.size];
					int index = 0;
					for (Element e : elemen) {
						for (PokemonAbilities pa : PokemonAbilityLoader.getInstance()) {
							if (pa.getName().equalsIgnoreCase(e.getChildByName("ability").getAttribute("value"))) {
								abilList[index] = pa;
								index++;
							}
						}		
					}
					pokemon.setAbility(abilList);
				}
				if (pokemons.getChildByName("hidden") != null) {
					for (PokemonAbilities pa : PokemonAbilityLoader.getInstance()) {
						if (pa.getName().equalsIgnoreCase(pokemons.getChildByName("hidden").getAttribute("value"))) {
							pokemon.setHiddenAbility(pa);
						}
					}
				}
				if (pokemons.getChildByName("compatibility") != null) {
					values = pokemons.getChildByName("compatibility").getAttribute("value");
					if (values != null) {
						String[] modify = values.split(",");
						pokemon.setCompatibility(modify);
					}
				}
				if (pokemons.getChildByName("hatch").getAttribute("value") != null) {
					pokemon.setNumStepsToHatch(Integer.parseInt(pokemons.getChildByName("hatch").getAttribute("value")));
				}
				if (pokemons.getChildByName("height").getAttribute("value") != null) {
					pokemon.setHeight(Float.parseFloat(pokemons.getChildByName("height").getAttribute("value")));
				}
				if (pokemons.getChildByName("weight").getAttribute("value") != null) {
					pokemon.setHeight(Float.parseFloat(pokemons.getChildByName("weight").getAttribute("value")));
				}
				if (pokemons.getChildByName("color").getAttribute("value") != null) {
					pokemon.setColor(pokemons.getChildByName("color").getAttribute("value"));
				}
				if (pokemons.getChildByName("habitat") != null) {
					if (pokemons.getChildByName("habitat").getAttribute("value") != null) {
						pokemon.setHabitat(pokemons.getChildByName("habitat").getAttribute("value"));
					}
				}
				if (pokemons.getChildByName("species").getAttribute("value") != null) {
					pokemon.setSpecies(pokemons.getChildByName("species").getAttribute("value"));
				}
				if (pokemons.getChildByName("positions") != null) {
					if (pokemons.getChildByName("positions").getAttribute("friendY") != null) {
						pokemon.setPlayerY(Integer.parseInt(pokemons.getChildByName("positions").getAttribute("friendY")));
					}
					if (pokemons.getChildByName("positions").getAttribute("foeY") != null) {
						pokemon.setEnemyY(Integer.parseInt(pokemons.getChildByName("positions").getAttribute("foeY")));
					}
					if (pokemons.getChildByName("positions").getAttribute("foeAltitude") != null) {
						pokemon.setAltitude(Integer.parseInt(pokemons.getChildByName("positions").getAttribute("foeAltitude")));
					}
				}
				pokedex.add(pokemon);
			}
		} catch (IOException e) {
//			new ExceptionHandler(this.getClass().getName(), "Was unable to locate types.xml");
		}
	}
	
	public static Collection<PokemonCreator> getInstance() {
		return Collections.unmodifiableCollection(pokedex);
	}
	
	public static PokemonCreator getPokemonById(int id) {
		return pokedex.get(id-1);
	}
	
	public static PokemonCreator getPokemonByName(String name) {
		for (PokemonCreator pokemon : pokedex) {
			if (pokemon.getName().equalsIgnoreCase(name)) {
				return pokemon;
			}
		}
		return null;
	}
	
}