DESCRIPTION = "The userspace read-copy update library by Mathieu Desnoyers"
HOMEPAGE = "http://lttng.org/urcu"
BUGTRACKER = "n/a"

LICENSE = "LGPLv2.1+ & MIT-style"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b472dc110d38774921e1d5ccb0065fff \
                    file://urcu.h;beginline=4;endline=32;md5=4de0d68d3a997643715036d2209ae1d9 \
                    file://urcu/uatomic/x86.h;beginline=4;endline=21;md5=220552f72c55b102f2ee35929734ef42"

PR = "r0"

SRC_URI = "http://lttng.org/files/urcu/userspace-rcu-${PV}.tar.bz2"

SRC_URI[md5sum] = "39ec317ef5af3ea167429f3eb2a93081"
SRC_URI[sha256sum] = "2914637c60027ea52974aa7fa8ee7f2a3a0067502bbaebe01de14d6988aa8882"

S = "${WORKDIR}/userspace-rcu-${PV}"
CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
inherit autotools

# liburcu, which is only used by lttng-ust, may not build on other
# platforms, like MIPS.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux'

