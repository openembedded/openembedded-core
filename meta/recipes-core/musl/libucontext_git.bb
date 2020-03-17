# Copyright (C) 2019 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "ucontext implementation featuring glibc-compatible ABI"
HOMEPAGE = "https://github.com/kaniini/libucontext"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=864cc1445419406b7093e8e531c9515e"
SECTION = "libs"
DEPENDS = ""

PV = "0.1.3+${SRCPV}"
SRCREV = "e6b4d7516dae9b200e94fcfcb9ebc9331389655f"
SRC_URI = "git://code.foxkit.us/adelie/libucontext.git;protocol=https \
           file://0001-pass-LDFLAGS-to-link-step.patch \
           file://0001-Makefile-Add-LIBDIR-variable.patch \
"

S = "${WORKDIR}/git"

COMPATIBLE_HOST = ".*-musl.*"

valid_archs = "\
i386 x86 \
ppc powerpc powerpc64 ppc64  \
arm aarch64 \
s390 \
"

def map_kernel_arch(a, d):
    import re

    valid_archs = d.getVar('valid_archs').split()

    if   re.match('(i.86|athlon)$', a):         return 'x86'
    elif re.match('x86.64$', a):                return 'x86_64'
    elif re.match('armeb$', a):                 return 'arm'
    elif re.match('aarch64$', a):               return 'aarch64'
    elif re.match('aarch64_be$', a):            return 'aarch64'
    elif re.match('aarch64_ilp32$', a):         return 'aarch64'
    elif re.match('aarch64_be_ilp32$', a):      return 'aarch64'
    elif re.match('mips(isa|)(32|64|)(r6|)(el|)$', a):      return 'mips'
    elif re.match('p(pc|owerpc)', a):           return 'ppc'
    elif re.match('p(pc64|owerpc64)', a):       return 'ppc64'
    elif re.match('riscv64$', a):               return 'riscv64'
    elif a in valid_archs:                      return a
    else:
        if not d.getVar("TARGET_OS").startswith("linux"):
            return a
        bb.error("cannot map '%s' to a linux kernel architecture" % a)

export ARCH = "${@map_kernel_arch(d.getVar('TARGET_ARCH'), d)}"

CFLAGS += "-Iarch/${ARCH}"

EXTRA_OEMAKE = "CFLAGS='${CFLAGS}' LDFLAGS='${LDFLAGS}' LIBDIR='${base_libdir}'"

do_compile() {
    oe_runmake ARCH=${ARCH}
}

do_install() {
    oe_runmake ARCH="${ARCH}" DESTDIR="${D}" install
}
