DESCRIPTION = "Replacement syslog API"
LICENSE = "BSD"
PR = "r1"

SRC_URI = "http://www.balabit.com/downloads/files/eventlog/0.2/${P}.tar.gz"

inherit autotools pkgconfig

do_stage () {
	autotools_stage_all
}
