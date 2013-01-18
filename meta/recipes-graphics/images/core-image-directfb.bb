LICENSE = "MIT"
PR="r0"


LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit core-image

IMAGE_INSTALL += "\
	${CORE_IMAGE_BASE_INSTALL} \
	packagegroup-core-basic \
	packagegroup-core-directfb \
"

python __anonymous () {
	packages = d.getVar('DISTRO_FEATURES', True).split()
	if "x11" in packages:
		raise bb.parse.SkipPackage("FEATURE \"x11\" is in DISTRO_FEATURES, Please remove \"x11\" from DISTRO_FEATURES, use \"directfb\" instead of it\n")
}
