package com.EudyContreras.Snake.PathFindingAI;

public class PortalWrapper {

	private CellNode portalIn;
	private CellNode portalOut;


	public PortalWrapper(){

	}

	public CellNode getPortalIn() {
		return portalIn;
	}

	public void setPortalIn(CellNode portalIn) {
		this.portalIn = portalIn;
	}

	public CellNode getPortalOut() {
		return portalOut;
	}

	public void setPortalOut(CellNode portalOut) {
		this.portalOut = portalOut;
	}

	public IndexWrapper getPortalInIndex(){
		return portalIn.getIndex();
	}

	public IndexWrapper getPortalOutIndex(){
		return portalOut.getIndex();
	}

}
