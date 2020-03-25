SUMMARY = "ACPICA tools for the development and debug of ACPI tables"
DESCRIPTION = "The ACPI Component Architecture (ACPICA) project provides an \
OS-independent reference implementation of the Advanced Configuration and \
Power Interface Specification (ACPI). ACPICA code contains those portions of \
ACPI meant to be directly integrated into the host OS as a kernel-resident \
subsystem, and a small set of tools to assist in developing and debugging \
ACPI tables."

HOMEPAGE = "http://www.acpica.org/"
SECTION = "console/tools"

LICENSE = "Intel | BSD | GPLv2"
LIC_FILES_CHKSUM = "file://source/compiler/aslcompile.c;beginline=7;endline=150;md5=6adbcb81e9ee6ae50c569b94fe12f7c5"

COMPATIBLE_HOST = "(i.86|x86_64|arm|aarch64).*-linux"

DEPENDS = "m4-native flex-native bison-native"

SRC_URI = "https://acpica.org/sites/acpica/files/acpica-unix-${PV}.tar.gz"
SRC_URI[md5sum] = "3505ba6170b77db1399eae0e2a959113"
SRC_URI[sha256sum] = "e77ab9f8557ca104f6e8f49efaa8eead29f78ca11cadfc8989012469ecc0738e"
UPSTREAM_CHECK_URI = "https://acpica.org/downloads"

S = "${WORKDIR}/acpica-unix-${PV}"

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "acpixtract acpidump"

EXTRA_OEMAKE = "CC='${CC}' \
                OPT_CFLAGS=-Wall \
                DESTDIR=${D} \
                PREFIX=${prefix} \
                INSTALLDIR=${bindir} \
                INSTALLFLAGS= \
                "

do_install() {
    oe_runmake install
}

# iasl*.bb is a subset of this recipe, so RREPLACE it
PROVIDES = "iasl"
RPROVIDES_${PN} += "iasl"
RREPLACES_${PN} += "iasl"
RCONFLICTS_${PN} += "iasl"

BBCLASSEXTEND = "native"
