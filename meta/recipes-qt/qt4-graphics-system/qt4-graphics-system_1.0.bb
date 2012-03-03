DESCRIPTION = "Set default Qt4 Graphics System to ${QT_GRAPHICS_SYSTEM}"
SECTION = "x11/base"
LICENSE = "MIT-X"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

QT_GRAPHICS_SYSTEM ?= "raster"

# xserver-common, x11-common
VIRTUAL-RUNTIME_xserver_common ?= "x11-common"

def _get_extra_rdepends(d):
    gs = d.getVar('QT_GRAPHICS_SYSTEM', True)
    if gs == "opengl":
        return "qt4-plugin-graphicssystems-glgraphicssystem"

    return ""

do_install () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d/
	cfg_file=${D}/${sysconfdir}/X11/Xsession.d/85xqt-graphicssystem
	echo "export QT_GRAPHICSSYSTEM=${QT_GRAPHICS_SYSTEM}" > $cfg_file
	chmod +x $cfg_file
}

RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_xserver_common} ${@_get_extra_rdepends(d)}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
