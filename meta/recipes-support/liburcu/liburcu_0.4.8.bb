DESCRIPTION = "The userspace read-copy update library by Mathieu Desnoyers"
HOMEPAGE = "http://lttng.org/urcu"
BUGTRACKER = "n/a"

LICENSE = "LGPLv2.1+ & MIT-style"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b472dc110d38774921e1d5ccb0065fff \
                    file://urcu.h;beginline=4;endline=32;md5=4de0d68d3a997643715036d2209ae1d9 \
                    file://urcu/uatomic_arch_x86.h;beginline=4;endline=21;md5=220552f72c55b102f2ee35929734ef42"

PR = "r0"

SRC_URI = "http://lttng.org/files/urcu/userspace-rcu-${PV}.tar.bz2"

S = "${WORKDIR}/userspace-rcu-${PV}"

inherit autotools

# liburcu, which is only used by lttng-ust, may not build on other
# platforms, e.g., on ARM, liburcu is only supported on ARMv7l while poky
# only supports armv4/armv4t/armv5te. 1 more example: liburcu doesn't support
# MIPS now.
# So here let us first suppport x86/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|powerpc.*)-linux'

