SUMMARY = "Hardware RNG based on CPU timing jitter"
DESCRIPTION = "The Jitter RNG provides a noise source using the CPU execution timing jitter. \
It does not depend on any system resource other than a high-resolution time \
stamp. It is a small-scale, yet fast entropy source that is viable in almost \
all environments and on a lot of CPU architectures."
HOMEPAGE = "https://www.chronox.de/jent"
LICENSE = "BSD-3-Clause OR GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fcaa2e2fa77c4ea22e45bf79e9a41093 \
                    file://LICENSE.gplv2;md5=eb723b61539feef013de476e68b5c50a \
                    file://LICENSE.bsd;md5=66a5cedaf62c4b2637025f049f9b826f \
                    "
SRC_URI = "git://github.com/smuellerDD/jitterentropy-library.git;branch=master;protocol=https;tag=v${PV} \
           file://0001-Use-pthreads-on-Linux-and-make-struct-jent_notime_ct.patch \
           "
SRCREV = "e783cf1c450bce4d72f95c9f9c84546a6094976a"

do_configure[noexec] = "1"

# The Makefile builds with -std=c11, which hides clock_gettime() on
# targets without a cycle counter in jitterentropy-base-user.h (arm, mips,
# riscv). Fixed upstream after 3.7.0.
CFLAGS += "-D_DEFAULT_SOURCE"

LDFLAGS += "-Wl,-O0"

do_install () {
    oe_runmake install INCDIR="/include" \
                       DESTDIR="${D}" \
                       PREFIX="${exec_prefix}" \
                       LIBDIR="${baselib}" \
                       INSTALL_STRIP="install"
}
