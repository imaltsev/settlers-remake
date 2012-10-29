package jsettlers.logic.algorithms.borders.traversing;

import jsettlers.common.movable.EDirection;
import jsettlers.common.position.ShortPoint2D;

public class BorderTraversingAlgorithm {

	/**
	 * Traverses the border of an area defined by the given {@link IContainingProvider} starting at {@link startPos}. The given visitor is called for
	 * every position on the outside of the area.<br>
	 * If the {@link startPos} is not surrounded by any position that is not in the area (meaning startPos is not on the border), the traversing can't
	 * be started and the visitor is never called.
	 * 
	 * @param containingProvider
	 *            {@link IContainingProvider} defining the position that are in and the ones that are outside the area.
	 * @param startPos
	 *            The start position for the traversing. This position must be in the area but at the border!
	 * @param visitor
	 *            The visitor that will be called for every border position (a border position is a position outside the border!).
	 */
	public static void traverseBorder(IContainingProvider containingProvider, ShortPoint2D startPos, IBorderVisitor visitor) {
		final int startInsideX = startPos.getX();
		final int startInsideY = startPos.getY();

		int insideX = startInsideX;
		int insideY = startInsideY;

		int outsideX = -1;
		int outsideY = -1;

		boolean foundOutsidePos = false;

		// determine first outside position
		for (EDirection dir : EDirection.values) {
			outsideX = insideX + dir.gridDeltaX;
			outsideY = insideY + dir.gridDeltaY;

			if (!containingProvider.contains(outsideX, outsideY)) {
				foundOutsidePos = true;
				break;
			}
		}

		if (!foundOutsidePos) { // no neighbor of the start position is on the outside.
			return;
		}

		final int startOutsideX = outsideX;
		final int startOutsideY = outsideY;

		visitor.visit(startOutsideX, startOutsideY);

		do {
			EDirection outInDir = EDirection.getDirection(insideX - outsideX, insideY - outsideY);
			EDirection neighborDir = outInDir.getNeighbor(-1);

			int neighborX = neighborDir.gridDeltaX + outsideX;
			int neighborY = neighborDir.gridDeltaY + outsideY;

			if (containingProvider.contains(neighborX, neighborY)) {
				insideX = neighborX;
				insideY = neighborY;
			} else {
				outsideX = neighborX;
				outsideY = neighborY;

				visitor.visit(outsideX, outsideY);
			}
		} while (insideX != startInsideX || insideY != startInsideY || outsideX != startOutsideX || outsideY != startOutsideY);
	}

}
