
import jason.environment.grid.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

//import java.util.logging.Logger;

/** class that implements the View of Domestic Robot application */
public class HouseView extends GridWorldView {
	private HouseModel hmodel;
	private int viewSize;
	private String currentDirectory;
	private int ownerPass;

	public HouseView(HouseModel model) {
		super(model, "Domestic Care Robot", model.GridSize);
		hmodel = model;
		ownerPass=0;
		viewSize = model.GridSize;
		setSize(viewSize, viewSize / 2);
		defaultFont = new Font("Arial", Font.BOLD, 14); // change default font
		setVisible(true);
		// repaint();
		currentDirectory = Paths.get("").toAbsolutePath().toString();
		// System.out.println("Directorio actual: " + currentDirectory);
	}

	/** draw application objects */
	@Override
	public void draw(Graphics g, int x, int y, int object) {
		Location lRobot = hmodel.getAgPos(0);
		Location lOwner = hmodel.getAgPos(1);
		Location lAuxiliar = hmodel.getAgPos(2);
		// Location lGuest = hmodel.getAgPos(2);
		Location loc = new Location(x, y);
		String objPath = currentDirectory;
		// super.drawAgent(g, x, y, Color.white, -1);
		g.setColor(Color.white);
		super.drawEmpty(g, x, y);
		// System.out.println("Directorio actual sigue siendo: " + currentDirectory);
		switch (object) {
			case HouseModel.BED:
				g.setColor(Color.lightGray);
				if (hmodel.lBed1.equals(loc)) {
					// objPath = currentDirectory.concat("/doc/doubleBedlt.png");
					// System.out.println("Cargo la imagen: "+objPath);
					drawMultipleScaledImage(g, x, y, "doc/doubleBedlt.png", 2, 2, 100, 100);
					// drawMultipleScaledImage(g, x, y, "doc/sofa.png", 2, 1, 90, 90);
					g.setColor(Color.red);
					super.drawString(g, x, y, defaultFont, " 1 ");
				}
				;
				if (hmodel.lBed2.equals(loc)) {
					objPath = "doc/singleBed.png";// currentDirectory.concat("/doc/singleBed.png");
					drawMultipleScaledImage(g, x, y, objPath, 2, 2, 60, 90);
					g.setColor(Color.red);
					super.drawString(g, x, y, defaultFont, " 2 ");
				}
				;
				if (hmodel.lBed3.equals(loc)) {
					objPath = "doc/singleBed.png";// currentDirectory.concat("/doc/singleBed.png");
					drawMultipleScaledImage(g, x, y, objPath, 2, 2, 60, 90);
					g.setColor(Color.red);
					super.drawString(g, x, y, defaultFont, " 3 ");
				}
				;
				break;
			case HouseModel.CHAIR:
				g.setColor(Color.lightGray);
				if (hmodel.lChair1.equals(loc)) {
					objPath = "doc/chairL.png";// currentDirectory.concat("/doc/chairL.png");
					drawScaledImageMd(g, x, y, objPath, 80, 80);
					// g.setColor(Color.red);
					// super.drawString(g, x, y, defaultFont, " 1 ");
				}
				;
				if (hmodel.lChair2.equals(loc)) {
					objPath = "doc/chairD.png";// currentDirectory.concat("/doc/chairD.png");
					drawScaledImageMd(g, x, y, objPath, 80, 80);
					// g.setColor(Color.red);
					// super.drawString(g, x, y, defaultFont, " 2 ");
				}
				;
				if (hmodel.lChair4.equals(loc)) {
					objPath = "doc/chairD.png";// currentDirectory.concat("/doc/chairD.png");
					drawScaledImageMd(g, x, y, objPath, 80, 80);
					// g.setColor(Color.red);
					// super.drawString(g, x, y, defaultFont, " 4 ");
				}
				;
				if (hmodel.lChair3.equals(loc)) {
					objPath = "doc/chairU.png";// currentDirectory.concat("/doc/chairU.png");
					drawScaledImageMd(g, x, y, objPath, 80, 80);
					// g.setColor(Color.red);
					// super.drawString(g, x, y, defaultFont, " 3 ");
				}
				;
				break;
			case HouseModel.SOFA:
				g.setColor(Color.lightGray);
				objPath = "doc/sofa.png";// currentDirectory.concat("/doc/sofa.png");
				drawMultipleScaledImage(g, x, y, objPath, 2, 1, 90, 90);
				// drawMultipleImage(g, x, y, "doc/sofa.png", 2, 1);
				break;
			case HouseModel.TABLE:
				g.setColor(Color.lightGray);
				objPath = "doc/table.png";// currentDirectory.concat("/doc/table.png");
				drawMultipleScaledImage(g, x, y, objPath, 2, 1, 80, 80);
				// drawMultipleImage(g, x, y, "doc/table.png", 2, 1);
				break;
			case HouseModel.DOOR:
				g.setColor(Color.lightGray);
				if (lRobot.equals(loc) | lRobot.isNeigbour(loc) |
						lOwner.equals(loc) | lOwner.isNeigbour(loc) | lAuxiliar.equals(loc) | lAuxiliar.isNeigbour(loc)) {// |
					// lGuest.equals(loc) | lGuest.isNeigbour(loc)) {
					objPath = "doc/openDoor2.png";// currentDirectory.concat("/doc/openDoor2.png");
					drawScaledImage(g, x, y, objPath, 75, 100);
					// super.drawAgent(g, x, y, Color.red, -1);
				} else {
					objPath = "doc/closeDoor2.png";// currentDirectory.concat("/doc/closeDoor2.png");
					drawScaledImage(g, x, y, objPath, 75, 100);
				}
				break;
			case HouseModel.WASHER:
				g.setColor(Color.lightGray);
				if (lRobot.equals(hmodel.lWasher)) {
					objPath = "doc/openWasher.png";// currentDirectory.concat("/doc/openWasher.png");
					drawScaledImage(g, x, y, objPath, 50, 60);
					// super.drawAgent(g, x, y, Color.red, -1);
				} else {
					objPath = "doc/closeWasher.png";// currentDirectory.concat("/doc/closeWasher.png");
					drawImage(g, x, y, objPath);
					// drawScaledImage(g, x, y, "doc/closeWasher.png", 50, 60);
				}
				break;
			case HouseModel.FRIDGE:
				g.setColor(Color.lightGray);
				if (lRobot.isNeigbour(hmodel.lFridge) || lOwner.isNeigbour(hmodel.lFridge) || lAuxiliar.isNeigbour(hmodel.lFridge)) {
					objPath = "doc/openNevera.png";// currentDirectory.concat("/doc/openNevera.png");
					drawImage(g, x, y, objPath);
					g.setColor(Color.yellow);
					// super.drawAgent(g, x, y, Color.red, -1);
				} else {
					objPath = "doc/closeNevera.png";// currentDirectory.concat("/doc/closeNevera.png");
					drawImage(g, x, y, objPath);
					g.setColor(Color.blue);
				}
				break;

			case HouseModel.DELIVER:
				g.setColor(Color.lightGray);
				objPath = "doc/entrega.png";
				drawImage(g, x, y, objPath);
				break;
			case HouseModel.RETRETE:
				g.setColor(Color.lightGray);
				if(hmodel.lRetrete.equals(new Location(x,y)))
				{
					objPath = "doc/retrete.png";
				}
				else
				{
					if((hmodel.getAgPos(0).equals(new Location(x,y))&& hmodel.getBateriaRobot(0)<199) || (hmodel.getAgPos(2).equals(new Location(x,y))&& hmodel.getBateriaRobot(2)<199))
					{
						objPath = "doc/cargadorCargando.png";
					}
					else
						objPath = "doc/cargador.png";
				}
				drawImage(g, x, y, objPath);
				break;
			case HouseModel.CABINET:
				g.setColor(Color.lightGray);
				if (lRobot.isNeigbour(hmodel.lCabinet) || lOwner.isNeigbour(hmodel.lCabinet)  || lAuxiliar.isNeigbour(hmodel.lFridge)) {
					objPath = "doc/openCabinet.png";
					drawImage(g, x, y, objPath);
					g.setColor(Color.yellow);

				} else {
					objPath = "doc/closeCabinet.png";
					drawImage(g, x, y, objPath);
					g.setColor(Color.blue);
				}
				drawString(g, x, y, defaultFont, "PA (" + hmodel.getAvailableParacetamol() + ")" + "IB (" + hmodel.getAvailableIbuprofeno() + ")" + "LO (" + hmodel.getAvailableLorazepam() + ")");
				drawString(g, x, y + 1, defaultFont, "AS (" + hmodel.getAvailableAspirina() + ")" + "AM (" + hmodel.getAvailableAmoxicilina() + ")");

				
				break;
		}
		repaint();
	}

	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		Location lRobot = hmodel.getAgPos(0);
		Location lOwner = hmodel.getAgPos(1);
		// Location lGuest = hmodel.getAgPos(2);
		String objPath = currentDirectory;

		if (id < 1) {
			if(hmodel.getBateriaRobot(0)<=0)
			{
				c = Color.yellow;
				objPath = "doc/botSinBateria.png";
				drawImage(g, x, y, objPath);
			}
			else if(!lRobot.equals(lOwner) && !lRobot.equals(hmodel.lFridge)) {
				c = Color.yellow;
				objPath = "doc/bot.png";// currentDirectory.concat("/doc/bot.png");
				drawImage(g, x, y, objPath);
				}
				;
				g.setColor(Color.black);
				super.drawString(g, x, y, defaultFont, "Rob");
			
		} else if (id == 1) {
			//drawMan(g, x, y, "down");
			if (lOwner.equals(hmodel.lChair1)) {
				drawMan(g, hmodel.lChair1.x, hmodel.lChair1.y, "left");
			} else if (lOwner.equals(hmodel.lChair2)) {
				drawMan(g, hmodel.lChair2.x, hmodel.lChair2.y, "down");
			}else if (lOwner.equals(hmodel.lRetrete))
			{
				drawMan(g, hmodel.lRetrete.x, hmodel.lRetrete.y, "down");
			}else if (lOwner.equals(hmodel.lBed3))
			{
				drawMan(g, hmodel.lBed3.x, hmodel.lBed3.y, "down");
			}else if (lOwner.equals(hmodel.lBed2))
			{
				drawMan(g, hmodel.lBed2.x, hmodel.lBed2.y, "down");
			
			} else if (lOwner.equals(hmodel.lChair4)) {
				drawMan(g, hmodel.lChair4.x, hmodel.lChair4.y, "down");
			} else if (lOwner.equals(hmodel.lChair3)) {
				drawMan(g, hmodel.lChair3.x, hmodel.lChair3.y, "right");
			}else if (lOwner.equals(hmodel.lBed1))
			{
				drawMan(g, hmodel.lBed1.x, hmodel.lBed1.y, "right");
			
			} else if (lOwner.equals(hmodel.lSofa)) {
				drawMan(g, hmodel.lSofa.x, hmodel.lSofa.y, "up");
			} else if (lOwner.equals(hmodel.lDeliver)) {
				g.setColor(Color.lightGray);
				objPath = "doc/openDoor2.png";// currentDirectory.concat("/doc/openDoor2.png");
				drawScaledImage(g, x, y, objPath, 75, 100);
				drawMan(g, x, y, "down");
			} else {
				switch(hmodel.getOwnerMove())
				{
					case 0:
						if(ownerPass==0)
						{
							drawMan(g, x, y, "walklu");
						}else
						{
							drawMan(g, x, y, "walklu");
						}
						break;
					case 1:
						if(ownerPass==0)
						{
							drawMan(g, x, y, "walklr");
						}else
						{
							drawMan(g, x, y, "walkrr");
						}
						break;
					case 2:
						if(ownerPass==0)
						{
							drawMan(g, x, y, "walkld");
						}else
						{
							drawMan(g, x, y, "walkrd");
						}
						break;
					case 3:
						if(ownerPass==0)
						{
							drawMan(g, x, y, "walkll");
						}else
						{
							drawMan(g, x, y, "walkrl");
						}
						break;
				}
				ownerPass=(ownerPass==1) ? 0:1;
			}
			;
		} else if (id==2)
		{
				c = Color.yellow;
				if(hmodel.getBateriaRobot(2)<=0)
				{
					objPath = "doc/auxiliarSinBateria.png";
				}
				else if(hmodel.getAuxiliarCargar()==0)
				{
					objPath = "doc/auxiliar.png";// currentDirectory.concat("/doc/bot.png");
				}
				else
				{
					objPath = "doc/auxiliarcaja.png";
				}
				drawImage(g, x, y, objPath);
				g.setColor(Color.black);
				super.drawString(g, x, y, defaultFont, "Aux");
		} else {

		}
	}

	private void drawMultipleObstacleH(Graphics g, int x, int y, int NCells) {
		for (int i = x; i < x + NCells; i++) {
			drawObstacle(g, i, y);
		}
	}

	private void drawMultipleObstacleV(Graphics g, int x, int y, int NCells) {
		for (int j = y; j < y + NCells; j++) {
			drawObstacle(g, x, j);
		}
	}

	private void drawMultipleImage(Graphics g, int x, int y, String imageAddress, int NW, int NH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + 2, y * cellSizeH + 2, NW * cellSizeW - 4, NH * cellSizeH - 4, null);
	}

	private void drawMultipleScaledImage(Graphics g, int x, int y, String imageAddress, int NW, int NH, int scaleW,
			int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		// ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + NW * cellSizeW * (100 - scaleW) / 200,
				y * cellSizeH + NH * cellSizeH * (100 - scaleH) / 200 + 1, NW * cellSizeW * scaleW / 100,
				NH * scaleH * cellSizeH / 100, null);
	}

	private void drawScaledImage(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image!" + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		// ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW * (100 - scaleW) / 200,
				y * cellSizeH + cellSizeH * (100 - scaleH) / 100, cellSizeW * scaleW / 100, scaleH * cellSizeH / 100,
				null);
	}

	private void drawScaledImageUp(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		// ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW * (100 - scaleW) / 200, y * cellSizeH + 2,
				cellSizeW * scaleW / 100, scaleH * cellSizeH / 100, null);
	}

	private void drawScaledImageLf(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		// ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW, y * cellSizeH + cellSizeH * (100 - scaleH) / 200 + 1,
				cellSizeW * scaleW / 100, scaleH * cellSizeH / 100, null);
	}

	private void drawScaledImageRt(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		// ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW * (100 - scaleW) / 100,
				y * cellSizeH + cellSizeH * (100 - scaleH) / 200 + 1, cellSizeW * scaleW / 100,
				scaleH * cellSizeH / 100, null);
	}

	private void drawScaledImageMd(Graphics g, int x, int y, String imageAddress, int scaleW, int scaleH) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		// ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + cellSizeW * (100 - scaleW) / 200,
				y * cellSizeH + cellSizeH * (100 - scaleH) / 200 + 1, cellSizeW * scaleW / 100,
				scaleH * cellSizeH / 100, null);
	}

	private void drawImage(Graphics g, int x, int y, String imageAddress) {
		URL url = getClass().getResource(imageAddress);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + imageAddress);
		else
			Img = new ImageIcon(getClass().getResource(imageAddress));
		// ImageIcon Img = new ImageIcon(getClass().getResource(imageAddress));
		// g.setColor(Color.lightGray);
		g.drawImage(Img.getImage(), x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 2, cellSizeH - 2, null);
	}

	private void drawMan(Graphics g, int x, int y, String how) {
		String resource = "doc/sitd.png";// currentDirectory.concat("/doc/sitd.png");
		switch (how) {
			case "right":
				resource = "doc/sitr.png";// currentDirectory.concat("/doc/sitr.png");
				break;
			case "left":
				resource = "doc/sitl.png";// currentDirectory.concat("/doc/sitl.png");
				break;
			case "up":
				resource = "doc/situ.png";// currentDirectory.concat("/doc/situ.png");
				break;
			case "down":
				resource = "doc/sitd.png";// currentDirectory.concat("/doc/sitd.png");
				break;
			case "stand":
				resource = "doc/sits.png";// currentDirectory.concat("/doc/sits.png");
				break;
			case "walkrr":
				resource = "doc/walkrr.png";// currentDirectory.concat("/doc/walklr.png");
				break;
			case "walklr":
				resource = "doc/walklr.png";// currentDirectory.concat("/doc/walklr.png");
				break;
			case "walklu":
				resource = "doc/walklu.png";// currentDirectory.concat("/doc/walklr.png");
				break;
			case "walkld":
				resource = "doc/walkld.png";// currentDirectory.concat("/doc/walklr.png");
				break;
			case "walkrd":
				resource = "doc/walkrd.png";// currentDirectory.concat("/doc/walklr.png");
				break;
			case "walkll":
				resource = "doc/walkll.png";// currentDirectory.concat("/doc/walklr.png");
				break;
			case "walkrl":
				resource = "doc/walkrl.png";// currentDirectory.concat("/doc/walklr.png");
				break;
		}
		URL url = getClass().getResource(resource);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + resource);
		else
			Img = new ImageIcon(getClass().getResource(resource));
		// ImageIcon Img = new ImageIcon(getClass().getResource(resource));
		g.drawImage(Img.getImage(), x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 3, cellSizeH - 3, null);
	}

	private void drawManSittingRight(Graphics g, int x, int y) {
		String objPath = "doc/sitr.png";// currentDirectory.concat("/doc/sitr.png");
		URL url = getClass().getResource(objPath);
		ImageIcon Img = new ImageIcon();
		if (url == null)
			System.out.println("Could not find image! " + objPath);
		else
			Img = new ImageIcon(getClass().getResource(objPath));
		// ImageIcon Img = new ImageIcon(getClass().getResource(objPath));
		g.drawImage(Img.getImage(), x * cellSizeW - 4, y * cellSizeH + 1, cellSizeW + 2, cellSizeH - 2, null);
	}

	private void drawSquare(Graphics g, int x, int y) {
		g.setColor(Color.blue);
		g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
		g.setColor(Color.cyan);
		g.drawRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 3, cellSizeH - 3);
	}

}
