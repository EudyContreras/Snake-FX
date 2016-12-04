package com.EudyContreras.Snake.PathFindingAI.BK;

public class Portal2D {

	private CellNode portalIn;
	private CellNode portalOut;


	public Portal2D(){

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

	public Index2D getPortalInIndex(){
		return portalIn.getIndex();
	}

	public Index2D getPortalOutIndex(){
		return portalOut.getIndex();
	}

}
