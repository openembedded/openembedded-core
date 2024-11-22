require xorg-driver-common.inc

python add_xorg_abi_depends() {
    _add_xorg_abi_depends(d, "video")
}
PACKAGEFUNCS =+ "add_xorg_abi_depends"
