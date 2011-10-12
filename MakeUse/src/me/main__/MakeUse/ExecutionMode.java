package me.main__.MakeUse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import me.main__.MakeUse.Executor.*;

public enum ExecutionMode {
	PERFORM ("perform", PerformExecutor.class, 2),
	DISPATCH ("dispatch", DispatchExecutor.class, 1),
	DIRECT ("direct", DirectExecutor.class, 0);
	
	private final String configstring;
	private final Class<? extends Executor> executorClass;
	private final int level;
	
	private static HashMap<String, ExecutionMode> lookupNamesParse = new HashMap<String, ExecutionMode>();
	private static HashMap<Integer, ExecutionMode> lookupNamesLevel = new HashMap<Integer, ExecutionMode>();
	
	private ExecutionMode(String configstring, Class<? extends Executor> executor, int level) {
		this.configstring = configstring;
		this.executorClass = executor;
		this.level = level;
	}
	
	/**
	 * @return How this should be expressed in the config.
	 */
	public String getConfigString() {
		return configstring;
	}
	
	/**
	 * @return The level of this ExecutionMode
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * @return A fresh executor. Should be initialized before using it!
	 * @throws NullPointerException When something whent wrong during the creation of the Executor
	 */
	public Executor getExecutor() throws NullPointerException {
		try {
			Constructor<? extends Executor> ctor = executorClass.getConstructor();
			Executor e = ctor.newInstance();
			e.setMode(this);
			return e;
		} catch (NoSuchMethodException e) {
			throw new NullPointerException("The executorClass is invalid!");
		} catch (SecurityException e) {
			throw new NullPointerException("The executorClass is invalid!");
		} catch (InstantiationException e) {
			throw new NullPointerException("The executorClass is invalid!");
		} catch (IllegalAccessException e) {
			throw new NullPointerException("The executorClass is invalid!");
		} catch (IllegalArgumentException e) {
			throw new NullPointerException("The executorClass is invalid!");
		} catch (InvocationTargetException e) {
			throw new NullPointerException("The executorClass is invalid!");
		}
	}
	
	/**
	 * Parses a string from the configuration.
	 * @param cmd
	 * The config-string
	 * @return
	 * An ExecutionMode
	 */
	public static ExecutionMode parse(String cmd) {
		return lookupNamesParse.get(cmd);
	}
	
	/**
	 * Gets an executor that's a level lower than the given ExecutionMode or null.
	 */
	public static ExecutionMode getFallback(ExecutionMode mode) {
		return lookupNamesLevel.get(mode.getLevel() + 1);
	}
	
	/**
	 * Gets this ExecutionMode's fallback.
	 */
	public ExecutionMode getFallback() {
		return ExecutionMode.getFallback(this);
	}
	
	static {
    	for (ExecutionMode c : values())
    	{
    		lookupNamesParse.put(c.getConfigString(), c);
    		lookupNamesLevel.put(c.getLevel(), c);
    	}
    }
}
