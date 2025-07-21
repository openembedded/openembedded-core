HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
DESCRIPTION = "U-Boot, a boot loader for Embedded boards based on PowerPC, \
ARM, MIPS and several other processors, which can be installed in a boot \
ROM and used to initialize and test the hardware or to download and run \
application code."
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native python3-setuptools-native"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
PE = "1"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "e37de002fac3895e8d0b60ae2015e17bb33e2b5b"

SRC_URI = "\
    git://source.denx.de/u-boot/u-boot.git;protocol=https;branch=master;tag=v${PV} \
    file://v3-0001-Makefile-scripts-Makefile.lib-fix-_efi.so-depende.patch \
    file://v3-0002-efi_loader-Makefile-change-apps-from-always-to-ta.patch \
    file://0001-nxp-Prepare-macros-for-KVM-changes.patch \
    file://0002-arm-io.h-Fix-io-accessors-for-KVM.patch \
    file://0003-qemu-arm-Enable-virtualizable-IO-accessors.patch \
"

SRC_URI_RISCV = "\
    file://u-boot-riscv-isa_clear.cfg \
    ${@bb.utils.contains    ("TUNE_FEATURES", "a",      "file://u-boot-riscv-isa_a.cfg", "", d)} \
    ${@bb.utils.contains    ("TUNE_FEATURES", "f",      "file://u-boot-riscv-isa_f.cfg", "", d)} \
    ${@bb.utils.contains    ("TUNE_FEATURES", "d",      "file://u-boot-riscv-isa_d.cfg", "", d)} \
    ${@bb.utils.contains_any("TUNE_FEATURES", "b zbb",  "file://u-boot-riscv-isa_zbb.cfg", "", d)} \
    ${@bb.utils.contains    ("TUNE_FEATURES", "zicbom", "file://u-boot-riscv-isa_zicbom.cfg", "", d)} \
    "

SRC_URI:append:riscv32 = "${SRC_URI_RISCV}"
SRC_URI:append:riscv64 = "${SRC_URI_RISCV}"

B = "${WORKDIR}/build"

inherit pkgconfig

do_configure[cleandirs] = "${B}"
