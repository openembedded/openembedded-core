DESCRIPTION = "Meta package for ensuring the build directory contains all appropriate toolchain packages for using an IDE"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS = "virtual/libc gdb-cross qemu-helper-native unfs-server-native"
PR = "r2"

inherit meta toolchain-scripts

do_populate_ide_support () {
  toolchain_create_tree_env_script
}

do_populate_ide_support[nostamp] = "1"
do_populate_ide_support[recrdeptask] = "do_package_write"
addtask populate_ide_support before do_build after do_install
