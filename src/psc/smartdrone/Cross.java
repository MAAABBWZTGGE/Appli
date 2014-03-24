package psc.smartdrone;

public class Cross {

	SimInterface sim;

	double crossX;
	double crossY;
	double crossZ;
	int crossInd;
	int crossPos;
	int cross;
	double dist;

	public Cross(SimInterface sim, int path, double dist) {
		this.sim = sim;
		this.dist = dist;
		cross = path;
		crossInd = 0;
		crossPos = 0;
		crossX = sim.getPathCoord(path, 0, 0);
		crossY = sim.getPathCoord(path, 0, 1);
		crossZ = sim.getPathCoord(path, 0, 2);
	}

	public void update() {
		if (cross > -1) {
			double crossDist = Math.sqrt(
					Math.pow(crossX - sim.getCoord(0), 2) +
					Math.pow(crossY - sim.getCoord(1), 2) +
					Math.pow(crossZ + sim.getCoord(2), 2));
			
			int crossNext = crossInd + 1;
			if (crossNext > sim.getPathLength(cross) - 1)
				crossNext = 0;

			double pDx = -sim.getPathCoord(cross, crossInd, 0)
					+ sim.getPathCoord(cross, crossNext, 0);
			double pDy = -sim.getPathCoord(cross, crossInd, 1)
					+ sim.getPathCoord(cross, crossNext, 1);
			double pDz = -sim.getPathCoord(cross, crossInd, 2)
					+ sim.getPathCoord(cross, crossNext, 2);
			double pDist = Math.sqrt(
					Math.pow(pDx, 2) +
					Math.pow(pDy, 2) +
					Math.pow(pDz, 2));

			if (pDist == 0) {
				crossPos = 0;
				crossInd += 1;
				if (crossInd > sim.getPathLength(cross) - 1)
					crossInd = 0;
				return;
			}
			
			double crossSpd = Math.exp(dist-crossDist);
			crossPos += crossSpd;
			if (crossPos > pDist) {
				crossPos = 0;
				crossInd += 1;
				if (crossInd > sim.getPathLength(cross) - 1)
					crossInd = 0;
			}
			
			crossX = sim.getPathCoord(cross, crossInd, 0) + pDx * crossPos / pDist;
			crossY = sim.getPathCoord(cross, crossInd, 1) + pDy * crossPos / pDist;
			crossZ = sim.getPathCoord(cross, crossInd, 2) + pDz * crossPos / pDist;

			//sim.tcp.sendVector(crossX, crossY, crossZ);
		}
	}
	
	public double getCoord(int c) {
		switch (c) {
		case 0:
			return crossX;
		case 1:
			return crossY;
		default:
			return crossZ;
		}
	}
}
