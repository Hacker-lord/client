package me.zeroeightsix.kami.util.math;

import me.zeroeightsix.kami.util.Wrapper
import net.minecraft.block.BlockAir
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.*

/**
 * Utilities for managing and transforming blockPos positions
 *
 * @author Qther / Vonr
 * Updated by dominikaaaa on 21/04/20.
 * Updated by Xiaro on 20/08/20
 */
object VectorUtils {

    /**
     * Gets distance between two vectors
     *
     * @param vecA First Vector
     * @param vecB Second Vector
     * @return the distance between two vectors
     */
    fun getDistance(vecA: Vec3d, vecB: Vec3d): Double {
        return sqrt((vecA.x - vecB.x).pow(2.0) + (vecA.y - vecB.y).pow(2.0) + (vecA.z - vecB.z).pow(2.0))
    }

    /**
     * Gets vectors between two given vectors (startVec and destinationVec) every (distance between the given vectors) / steps
     *
     * @param startVec Beginning vector
     * @param destinationVec Ending vector
     * @param steps distance between given vectors
     * @return all vectors between startVec and destinationVec divided by steps
     */
    fun extendVec(startVec: Vec3d, destinationVec: Vec3d, steps: Int): ArrayList<Vec3d> {
        val returnList = ArrayList<Vec3d>(steps + 1)
        val stepDistance = getDistance(startVec, destinationVec) / steps
        for (i in 0 until max(steps, 1) + 1) {
            returnList.add(advanceVec(startVec, destinationVec, stepDistance * i))
        }
        return returnList
    }

    /**
     * Moves a vector towards a destination based on distance
     *
     * @param startVec Starting vector
     * @param destinationVec returned vector
     * @param distance distance to move startVec by
     * @return vector based on startVec that is moved towards destinationVec by distance
     */
    fun advanceVec(startVec: Vec3d, destinationVec: Vec3d, distance: Double): Vec3d {
        val advanceDirection = destinationVec.subtract(startVec).normalize()
        return if (destinationVec.distanceTo(startVec) < distance) destinationVec
        else advanceDirection.scale(distance)
    }

    /**
     * Get all rounded block positions inside a 3-dimensional area between pos1 and pos2.
     *
     * @param pos1 Starting vector
     * @param pos2 Ending vector
     * @return rounded block positions inside a 3d area between pos1 and pos2
     */
    fun getBlockPositionsInArea(pos1: Vec3d, pos2: Vec3d): List<BlockPos> {
        val minX = (min(pos1.x, pos2.x)).roundToInt()
        val maxX = (max(pos1.x, pos2.x)).roundToInt()
        val minY = (min(pos1.y, pos2.y)).roundToInt()
        val maxY = (max(pos1.y, pos2.y)).roundToInt()
        val minZ = (min(pos1.z, pos2.z)).roundToInt()
        val maxZ = (max(pos1.z, pos2.z)).roundToInt()
        return getBlockPos(minX, maxX, minY, maxY, minZ, maxZ)
    }

    /**
     * Get all block positions inside a 3d area between pos1 and pos2
     *
     * @param pos1 Starting blockPos
     * @param pos2 Ending blockPos
     * @return block positions inside a 3d area between pos1 and pos2
     */
    fun getBlockPositionsInArea(pos1: BlockPos, pos2: BlockPos): List<BlockPos> {
        val minX = min(pos1.x, pos2.x)
        val maxX = max(pos1.x, pos2.x)
        val minY = min(pos1.y, pos2.y)
        val maxY = max(pos1.y, pos2.y)
        val minZ = min(pos1.z, pos2.z)
        val maxZ = max(pos1.z, pos2.z)
        return getBlockPos(minX, maxX, minY, maxY, minZ, maxZ)
    }

    /**
     * Get a block pos with the Y level as the highest terrain level
     *
     * @param pos blockPos
     * @return blockPos with highest Y level terrain
     */
    fun getHighestTerrainPos(pos: BlockPos): BlockPos {
        for (i in pos.y downTo 0) {
            val block = Wrapper.world!!.getBlockState(BlockPos(pos.getX(), i, pos.getZ())).block
            val replaceable = Wrapper.world!!.getBlockState(BlockPos(pos.getX(), i, pos.getZ())).material.isReplaceable
            if (block !is BlockAir && !replaceable) {
                return BlockPos(pos.getX(), i, pos.getZ())
            }
        }
        return BlockPos(pos.getX(), 0, pos.getZ())
    }

    private fun getBlockPos(minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int): List<BlockPos> {
        val returnList = ArrayList<BlockPos>()
        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                for (y in minY..maxY) {
                    returnList.add(BlockPos(x, y, z))
                }
            }
        }
        return returnList
    }
}
