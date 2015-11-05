package khang.iwcjff.afsj;

public abstract class AbstExtensionGenerator implements IExtensionGenerator {
	
    protected int max;
    protected int current;
    
	@Override public boolean hasNext() {
		return current < max;
	}
}
