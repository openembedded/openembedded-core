SUMMARY = "OpenPrinting printer support - filters"
DESCRIPTION = "Foomatic is a printer database designed to make it easier to set up \
common printers for use with UNIX-like operating systems.\
It provides the "glue" between a print spooler (like CUPS or lpr) and \
the printer, by processing files sent to the printer. \
 \
This package consists of filters used by the printer spoolers \
to convert the incoming PostScript data into the printer's native \
format using a printer-specific, but spooler-independent PPD file. \
"

DEPENDS += "cups perl libxml2"
PR = "r1"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${WORKDIR}/foomatic-filters-${PV}/COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = "http://www.openprinting.org/download/foomatic/foomatic-filters-${PV}.tar.gz"

SRC_URI[md5sum] = "ec907d9ea17e03bccb91db66e9470586"
SRC_URI[sha256sum] = "bedc61466c284c8ea0ae95c97d51a873411c6c55338e67831893772465f5434d"

inherit autotools

EXTRA_OECONF += "--disable-file-converter-check --with-file-converter=texttops"

do_configure_prepend() {
    export PERL="${bindir}/env perl"
    export LIB_CUPS=${exec_prefix}/lib/cups               # /usr/lib NOT libdir
    export CUPS_BACKENDS=${exec_prefix}/lib/cups/backend  # /usr/lib NOT libdir
    export CUPS_FILTERS=${exec_prefix}/lib/cups/filter    # /usr/lib NOT libdir
    export CUPS_PPDS=${datadir}/cups/model
}

do_install_append_linuxstdbase() {
    install -d ${D}${exec_prefix}/lib/cups/filter
    ln -sf ${bindir}/foomatic-rip ${D}${exec_prefix}/lib/cups/filter
}

FILES_${PN} += "${exec_prefix}/lib/cups/ ${exec_prefix}/lib/ppr/"
