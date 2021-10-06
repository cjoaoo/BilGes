package business.utils;

public class MockPagamento {
	
	private static int entidade = 12345;
	private static int referencia = 123456789;
	private float valor;
	
	private MockPagamento(float valor) {
		this.valor = valor;
	}
	
	public static MockPagamento getDadosPagamento(float valor) {
		return new MockPagamento(valor);
	}
	
	public int getEntidade() {
		return entidade;
	}
	
	public int getReferencia() {
		return referencia;
	}


}
