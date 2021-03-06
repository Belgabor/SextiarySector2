package shift.sextiarysector.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import shift.sextiarysector.api.gearforce.tileentity.EnergyStorage;
import shift.sextiarysector.api.gearforce.tileentity.IGearForceGrid;
import shift.sextiarysector.api.gearforce.tileentity.IGearForceHandler;
import shift.sextiarysector.container.ItemBox;

public class TileEntityShopMonitor extends TileEntityDirection implements IGearForceHandler, IGearForceGrid {

    public EnergyStorage storage = new EnergyStorage("Base", 1, 10000);
    protected ItemBox items = new ItemBox("memory", 1);

    public boolean on;

    @Override
    public void updateEntity() {

        if (this.storage.getSpeedStored() > 0 && on) {
            this.storage.drawEnergy(1, 20, false);

            if (this.storage.getSpeedStored() == 0) {
                this.changeON();
            }

        }

    }

    public void changeON() {

        if (!this.worldObj.isRemote) {
            if (!this.on && this.storage.getSpeedStored() > 0) {
                this.on = true;
                this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 4);
            } else {
                this.on = false;
                this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 4);
            }
            this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.click", 0.3F, 0.6F);
        }

        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public ItemStack getMemory() {

        return this.items.getStackInSlot(0);

    }

    public void setMemory(ItemStack item) {
        if (item != null) item.stackSize = 1;
        this.items.setInventorySlotContents(0, item);
    }

    //NBT関係
    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);
        storage.readFromNBT(nbt);
        this.on = nbt.getBoolean("on");
        items.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);
        storage.writeToNBT(nbt);
        nbt.setBoolean("on", on);
        items.writeToNBT(nbt);
    }

    @Override
    public int addEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
        if (!this.canInterface(from)) return 0;
        int i = this.storage.addEnergy(power, speed, simulate);
        return i;
    }

    @Override
    public int drawEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
        if (!this.canInterface(from)) return 0;
        return 0;
    }

    @Override
    public boolean canInterface(ForgeDirection from) {
        return ForgeDirection.DOWN.equals(from);
    }

    @Override
    public int getPowerStored(ForgeDirection from) {
        if (!this.canInterface(from)) return 0;
        return 0;
    }

    @Override
    public int getSpeedStored(ForgeDirection from) {
        if (!this.canInterface(from)) return 0;
        return 0;
    }

    @Override
    public int getMaxPowerStored(ForgeDirection from) {
        if (!this.canInterface(from)) return 0;
        return this.storage.getMaxPower();
    }

    @Override
    public int getMaxSpeedStored(ForgeDirection from) {
        if (!this.canInterface(from)) return 0;
        return this.storage.getMaxSpeed();
    }

    @Override
    public boolean canIn(ForgeDirection from) {
        return from.ordinal() == ForgeDirection.DOWN.ordinal();
    }

    @Override
    public boolean canOut(ForgeDirection from) {
        return false;
    }

}