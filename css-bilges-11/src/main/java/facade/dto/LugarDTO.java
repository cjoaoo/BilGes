package facade.dto;

public class LugarDTO {
	
	private String letra;
	private int num;
	
	public LugarDTO(String letra, int num) {
		this.letra = letra;
		this.num = num;
	}
	
	public String getLetra() {
		return letra;
	}
	
	public int getNum() {
		return num;
	}
	
	@Override
	public String toString() {
		return letra + "-" + num;
	}

}
