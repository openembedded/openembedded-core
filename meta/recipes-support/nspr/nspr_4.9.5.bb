DESCRIPTION = "Netscape Portable Runtime Library"
HOMEPAGE =  "http://www.mozilla.org/projects/nspr/"
LICENSE = "GPL-2.0 | MPL-2.0 | LGPL-2.1"
LIC_FILES_CHKSUM = "file://configure.in;beginline=3;endline=6;md5=90c2fdee38e45d6302abcfe475c8b5c5 \
                    file://Makefile.in;beginline=4;endline=38;md5=beda1dbb98a515f557d3e58ef06bca99"
SECTION = "libs/network"

PR = "r0"

SRC_URI = "ftp://ftp.mozilla.org/pub/mozilla.org/nspr/releases/v${PV}/src/nspr-${PV}.tar.gz \
           file://remove-rpath-from-tests.patch \
           file://fix-build-on-x86_64.patch \
           file://trickly-fix-build-on-x86_64.patch \
           file://fix-build-on-aarch64.patch \
          "

SRC_URI += "file://nspr.pc.in"

RDEPENDS_${PN}-dev += "perl"

SRC_URI[md5sum] = "b6ccfa8fcbbeb17ebeb19a3edff612bd"
SRC_URI[sha256sum] = "616ab65c849155c9ed0e5f502530a241cc9108e278275aa448b417ae632c7604"

S = "${WORKDIR}/nspr-${PV}/mozilla/nsprpub"

TESTS = "runtests.pl \
    runtests.sh \
    accept \
    acceptread \
    acceptreademu \
    affinity \
    alarm \
    anonfm \
    atomic \
    attach \
    bigfile \
    cleanup \
    cltsrv  \
    concur \
    cvar \
    cvar2 \
    dlltest \
    dtoa \
    errcodes \
    exit \
    fdcach \
    fileio \
    foreign \
    formattm \
    fsync \
    gethost \
    getproto \
    i2l \
    initclk \
    inrval \
    instrumt \
    intrio \
    intrupt \
    io_timeout \
    ioconthr \
    join \
    joinkk \
    joinku \
    joinuk \
    joinuu \
    layer \
    lazyinit \
    libfilename \
    lltest \
    lock \
    lockfile \
    logfile \
    logger \
    many_cv \
    multiwait \
    nameshm1 \
    nblayer \
    nonblock \
    ntioto \
    ntoh \
    op_2long \
    op_excl \
    op_filnf \
    op_filok \
    op_nofil \
    parent \
    parsetm \
    peek \
    perf \
    pipeping \
    pipeping2 \
    pipeself \
    poll_nm \
    poll_to \
    pollable \
    prftest \
    primblok \
    provider \
    prpollml \
    ranfile \
    randseed \
    reinit \
    rwlocktest \
    sel_spd \
    selct_er \
    selct_nm \
    selct_to \
    selintr \
    sema \
    semaerr \
    semaping \
    sendzlf \
    server_test \
    servr_kk \
    servr_uk \
    servr_ku \
    servr_uu \
    short_thread \
    sigpipe \
    socket \
    sockopt \
    sockping \
    sprintf \
    stack \
    stdio \
    str2addr \
    strod \
    switch \
    system \
    testbit \
    testfile \
    threads \
    timemac \
    timetest \
    tpd \
    udpsrv \
    vercheck \
    version \
    writev \
    xnotify \
    zerolen"

inherit autotools

do_configure() {
	gnu-configize --force
	mv config.sub config.guess build/autoconf
	oe_runconf
}

do_compile_prepend() {
	oe_runmake CROSS_COMPILE=1 CFLAGS="-DXP_UNIX" LDFLAGS="" CC=gcc -C config export
}

do_compile_append() {
	oe_runmake -C pr/tests
}

do_install_append() {
    install -D ${WORKDIR}/nspr.pc.in ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OEPREFIX:${prefix}:g ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OELIBDIR:${libdir}:g ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OEINCDIR:${includedir}:g ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OEEXECPREFIX:${exec_prefix}:g ${D}${libdir}/pkgconfig/nspr.pc
    cd ${S}/pr/tests
    mkdir -p ${D}${libdir}/nspr/tests
    install -m 0755 ${TESTS} ${D}${libdir}/nspr/tests

    # delete compile-et.pl and perr.properties from ${bindir} because these are
    # only used to generate prerr.c and prerr.h files from prerr.et at compile
    # time
    rm ${D}${bindir}/compile-et.pl ${D}${bindir}/prerr.properties
}

FILES_${PN} = "${libdir}/lib*.so"
FILES_${PN}-dev = "${bindir}/* ${libdir}/nspr/tests/* ${libdir}/pkgconfig \
                ${includedir}/* ${datadir}/aclocal/* "
FILES_${PN}-dbg += "${libdir}/nspr/tests/.debug/*"
