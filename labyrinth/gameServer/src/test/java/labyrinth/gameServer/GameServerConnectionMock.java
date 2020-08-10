package labyrinth.gameServer;

import labyrinth.contracts.communication.dtos.responses.ResponseDtoBase;
import labyrinth.contracts.entities.lobby.LobbyUser;

/**
 * Mock for @see IGameServerConnection
 * @author Lukas Reinhardt
 * @version 1.0
 */
public final class GameServerConnectionMock implements IGameServerConnection
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
	
	LobbyUser user;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LobbyUser getUser() { return user; }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUser(LobbyUser value) { user = value; }
	
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
