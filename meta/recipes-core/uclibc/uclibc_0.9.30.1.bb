# UCLIBC_BASE should be the latest released revision of uclibc (that way
# the config files will typically be correct!)  uclibc-cvs takes precedence
# over uclibc-${UCLIBC_BASE}, if a config file in uclibc-cvs is out of date
# try removing it
#
# UCLIBC_BASE can be set in a distro file, but whether this works depends
# on whether the base patches apply to the selected (SRCDATE) svn release.
#
UCLIBC_BASE ?= "0.9.30.1"

require uclibc.inc
PR = "r1"

PROVIDES += "virtual/${TARGET_PREFIX}libc-for-gcc"

SRC_URI += "file://uClibc.machine file://uClibc.distro \
            file://arm-linuxthreads.patch;patch=1 \
            file://linuxthreads-changes.patch;patch=1 \
	    file://pthread_atfork.patch;patch=1 \
	    file://uclibc_ldso_use_O0.patch;patch=1 \
	    file://ldso_use_arm_dl_linux_resolve_in_thumb_mode.patch;patch=1 \
	    file://gcc-4.4-fixlet.patch;patch=1 \
	    file://uclibc-c99-ldbl-math.patch;patch=1 \
	    file://Use-__always_inline-instead-of-__inline__.patch;patch=1 \
	   "
#recent versions uclibc require real kernel headers
PACKAGE_ARCH = "${MACHINE_ARCH}"

#as stated above, uclibc needs real kernel-headers
#however: we can't depend on virtual/kernel when nptl hits due to depends deadlocking ....
KERNEL_SOURCE = "${STAGING_DIR_HOST}/${exec_prefix}"

S = "${WORKDIR}/uClibc-${UCLIBC_BASE}"
