package com.cshen.tiyu.domain.ball;

import java.io.Serializable;


public class BasketBallScroeList  implements Serializable{
	private static final long serialVersionUID = -7060210544600464481L; 

	ScroeSF SF;//胜负
	ScroeRFSF RFSF;//让分胜负
	ScroeSFC SFC;//胜负差
	ScroeDXF DXF;//大小分
	
	public ScroeSF getSF() {
		return SF;
	}
	public void setSF(ScroeSF sF) {
		SF = sF;
	}
	public ScroeRFSF getRFSF() {
		return RFSF;
	}
	public void setRFSF(ScroeRFSF rFSF) {
		RFSF = rFSF;
	}
	public ScroeSFC getSFC() {
		return SFC;
	}
	public void setSFC(ScroeSFC sFC) {
		SFC = sFC;
	}
	public ScroeDXF getDXF() {
		return DXF;
	}
	public void setDXF(ScroeDXF dXF) {
		DXF = dXF;
	}

}
