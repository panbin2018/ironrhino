package org.ironrhino.sample.remoting;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.ironrhino.core.remoting.Remoting;
import org.springframework.security.core.userdetails.UserDetails;

@Remoting
public interface TestService {

	public void ping();

	public void throwException(String message) throws Exception;

	public String echo();

	public String echo(String str);

	public List<String> echoList(List<String> list);

	public List<String[]> echoListWithArray(List<String[]> list);

	public int countAndAdd(List<String> list, int param);

	public String[] echoArray(String[] arr);

	public UserDetails loadUserByUsername(String username);

	public List<UserDetails> search(String keyword);

	public Optional<UserDetails> loadOptionalUserByUsername(String username);

	public Future<UserDetails> loadFutureUserByUsername(String username);
	
	public Callable<UserDetails> loadCallableUserByUsername(String username);

}
