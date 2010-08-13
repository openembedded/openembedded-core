DESCRIPTION = "Meta package for ensuring the build directory contains all appropriate toolchain packages for using an IDE"
LICENSE = "MIT"
DEPENDS = "virtual/libc gdb-cross"
PR = "r1"

inherit meta toolchain-scripts

do_populate_ide_support () {
  toolchain_create_tree_env_script
}

do_populate_ide_support[nostamp] = "1"
do_populate_ide_support[recrdeptask] = "do_package_write"
addtask populate_ide_support before do_build after do_install
