package com.EudyContreras.Snake.PathFindingAI;

public class Portal2D {

	private CellNode portalIn;
	private CellNode portalOut;


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
