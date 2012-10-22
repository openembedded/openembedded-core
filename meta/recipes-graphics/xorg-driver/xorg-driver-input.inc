include xorg-driver-common.inc

DEPENDS += "inputproto kbproto "

python populate_packages_prepend() {
    add_abi_depends(d, "input")
}

FILES_${PN} += " ${libdir}/xorg/modules/input/*.so"
FILES_${PN}-dbg += " ${libdir}/xorg/modules/input/.debug"
