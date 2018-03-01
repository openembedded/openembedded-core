require systemd.inc
FILESEXTRAPATHS =. "${FILE_DIRNAME}/systemd:"

DEPENDS = "intltool-native libcap util-linux gnu-efi gperf-native"

SRC_URI += "file://0007-use-lnr-wrapper-instead-of-looking-for-relative-opti.patch"

inherit autotools pkgconfig gettext
inherit deploy

EFI_CC ?= "${CC}"
# Man pages are packaged through the main systemd recipe
EXTRA_OECONF = " --enable-gnuefi \
                 --with-efi-includedir=${STAGING_INCDIR} \
                 --with-efi-ldsdir=${STAGING_LIBDIR} \
                 --with-efi-libdir=${STAGING_LIBDIR} \
                 --disable-manpages \
                 EFI_CC='${EFI_CC}' \
               "

# install to the image as boot*.efi if its the EFI_PROVIDER,
# otherwise install as the full name.
# This allows multiple bootloaders to coexist in a single image.
python __anonymous () {
    import re
    target = d.getVar('TARGET_ARCH')
    prefix = "" if d.getVar('EFI_PROVIDER', True) == "systemd-boot" else "systemd-"
    if target == "x86_64":
        systemdimage = prefix + "bootx64.efi"
    else:
        systemdimage = prefix + "bootia32.efi"
    d.setVar("SYSTEMD_BOOT_IMAGE", systemdimage)
    prefix = "systemd-" if prefix == "" else ""
    d.setVar("SYSTEMD_BOOT_IMAGE_PREFIX", prefix)
}

FILES_${PN} = "/boot/EFI/BOOT/${SYSTEMD_BOOT_IMAGE}"

RDEPENDS_${PN} += "virtual/systemd-bootconf"

# Imported from the old gummiboot recipe
TUNE_CCARGS_remove = "-mfpmath=sse"
COMPATIBLE_HOST = "(x86_64.*|i.86.*)-linux"
COMPATIBLE_HOST_x86-x32 = "null"

do_compile() {
	SYSTEMD_BOOT_EFI_ARCH="ia32"
	if [ "${TARGET_ARCH}" = "x86_64" ]; then
		SYSTEMD_BOOT_EFI_ARCH="x64"
	fi

	oe_runmake ${SYSTEMD_BOOT_IMAGE_PREFIX}${SYSTEMD_BOOT_IMAGE}
}

do_install() {
	install -d ${D}/boot
	install -d ${D}/boot/EFI
	install -d ${D}/boot/EFI/BOOT
	install ${B}/systemd-boot*.efi ${D}/boot/EFI/BOOT/${SYSTEMD_BOOT_IMAGE}
}

do_deploy () {
	install ${B}/systemd-boot*.efi ${DEPLOYDIR}
}
addtask deploy before do_build after do_compile
