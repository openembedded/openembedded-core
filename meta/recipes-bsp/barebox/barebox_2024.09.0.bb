SUMMARY = "barebox is a bootloader designed for embedded systems. It runs on a variety of architectures including x86, ARM, MIPS, PowerPC and others."
DESCRIPTION = "barebox aims to be a versatile and flexible bootloader not only for booting embedded Linux systems, \
but also for initial hardware bringup and development. \
Users should feel right at home with a shell with UNIX-like virtual file system access to hardware, \
Linux kernel driver API for making driver porting easier, \
and a subset of the POSIX C library for writing more command-line utilities."
HOMEPAGE = "https://barebox.org/"
SECTION = "bootloaders"

LIC_FILES_CHKSUM = "file://COPYING;md5=f5125d13e000b9ca1f0d3364286c4192"

inherit barebox

SRC_URI = "https://barebox.org/download/barebox-${PV}.tar.bz2"
SRC_URI[sha256sum] = "4d4ea7e232aeba2b3cb9ccf0dbf8cd8e01c65b6c615f64c3bd0c4f24f23a47da"
