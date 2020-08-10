package labyrinth.registrationServer;

import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;
import labyrinth.contracts.entities.lobby.GameServerEntry;

/**
 * Mock for @see IRegistrationServerConnection
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class RegistrationServerConnectionMock implements IRegistrationServerConnection
{
	//Properties
	
    private String ip;
    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getIp() { return ip; }
	
	private boolean wasCanceled;
	/**
	 * Corresponding getter
	 * @return If cancel() was called
	 */
	public boolean getWasCanceled() { return wasCanceled; }
	
	private ResponseDtoBase lastResponseSent;
	/**
	 * Corresponding getter
	 * @return the last response sent back to the client
	 */
	public ResponseDtoBase getLastResponseSent() { return lastResponseSent; }
	
	private int userId;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUserId() { return userId; }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserId(int value) { userId = value; }

	private GameServerEntry server;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameServerEntry getServer() { return server; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServer(GameServerEntry value) { server = value; }
	
	//Methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <ResponseT extends ResponseDtoBase> void sendResponse(ResponseT response) { lastResponseSent = response; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancel() { wasCanceled = true; }
}
