SECTION = "devel"
SUMMARY = "Linux Trace Toolkit Control"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed \
to extract program execution details from the Linux operating system \
and interpret them."

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=ab682a4729389c3f11913d758affe98e \
                    file://gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://lgpl-2.1.txt;md5=0f0d71500e6a57fd24d825f33242b9ca"

DEPENDS = "liburcu popt lttng2-ust"

SRCREV = "9beed4cb465347c778e4f982c330cef78f2628b7"
PV = "v2.0.pre16+git${SRCREV}"
PR = "r0"

SRC_URI = "git://git.lttng.org/lttng-tools.git;protocol=git\
           file://lttng-tools-fix-compiling-error-for-powerpc-arm.patch"

S = "${WORKDIR}/git"

inherit autotools

export KERNELDIR="${STAGING_KERNEL_DIR}"

# Due to liburcu not building for MIPS currently this recipe needs to
# be limited also.
# So here let us first suppport x86/arm/powerpc platforms now.
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux'
