DESCRIPTION = "A boot profiling tool"
HOMEPAGE = "http://code.google.com/p/ubootchart/"
LICENSE="GPLv3"

SRC_URI = "svn://ubootchart.googlecode.com/svn/;proto=http;module=trunk \
        file://sysvinit.patch;patch=1;pnum=0"

S = "${WORKDIR}/trunk"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${LIBS} ${INCLUDES} ${S}/ubootchartd_bin.c -o ubootchartd_bin
}

do_install() {
        install -m 0755 -d ${D}/sbin ${D}/etc/ubootchart
        install -m 0755 ${S}/ubootchartd_bin ${D}/sbin
        install -m 0755 ${S}/ubootchartd ${D}/sbin
        install -m 0644 ${S}/ubootchart.conf ${D}/etc/ubootchart
        install -m 0755 ${S}/start.sh ${D}/etc/ubootchart
        install -m 0755 ${S}/finish.sh ${D}/etc/ubootchart
}

