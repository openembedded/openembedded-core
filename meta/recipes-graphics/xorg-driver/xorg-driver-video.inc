include xorg-driver-common.inc

DEPENDS =+ "renderproto videoproto xextproto fontsproto"

python populate_packages_prepend() {
    add_abi_depends(d, "video")
}
