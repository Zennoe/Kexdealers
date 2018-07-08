package ecs;

public class CharacterSheetComponent extends Component {

	private int eID;
	
	private String name = "default";
	
	private int level = 1;
	
	private int maxHealth;
	private int remHealth;
	private int attack;
	
	
	
	public CharacterSheetComponent(int eID) {
		this.eID = eID;
	}
	
	@Override
	public int getEID() {
		return eID;
	}

	@Override
	public void setEID(int eID) {
		this.eID = eID;
	}

	@Override
	public CharacterSheetComponent clone() {
		CharacterSheetComponent deepCopy = new CharacterSheetComponent(this.eID)
				.setName(this.name)
				.setLevel(this.level)
				.setMaxHealth(this.maxHealth)
				.setRemainingHealth(this.remHealth)
				.setAttack(this.attack);
		return deepCopy;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("CharacterSheetComponent").append(eID).append(">");
		s.append("(");
		s.append(" Name: ").append(name);
		s.append(" Lvl: ").append(level);
		s.append(" HP: ").append(remHealth).append("/").append(maxHealth);
		s.append(" ATK: ").append(attack);
		s.append(" )");
		return s.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public CharacterSheetComponent setName(String name) {
		this.name = name;
		return this;
	}
	
	public int getLevel() {
		return level;
	}
	
	public CharacterSheetComponent setLevel(int level) {
		this.level = level;
		return this;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public CharacterSheetComponent setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		return this;
	}
	
	public int getRemainingHealth() {
		return remHealth;
	}
	
	public CharacterSheetComponent setRemainingHealth(int remHealth) {
		this.remHealth = remHealth;
		return this;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public CharacterSheetComponent setAttack(int attack) {
		this.attack = attack;
		return this;
	}
}
