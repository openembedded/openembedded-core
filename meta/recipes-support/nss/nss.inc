SUMMARY = "Mozilla's SSL and TLS implementation"
DESCRIPTION = "Network Security Services (NSS) is a set of libraries \
designed to support cross-platform development of \
security-enabled client and server applications. \
Applications built with NSS can support SSL v2 and v3, \
TLS, PKCS 5, PKCS 7, PKCS 11, PKCS 12, S/MIME, X.509 \
v3 certificates, and other security standards."
HOMEPAGE = "http://www.mozilla.org/projects/security/pki/nss/"
SECTION = "libs"

LICENSE = "MPL-2.0 | (MPL-2.0 & GPL-2.0+) | (MPL-2.0 & LGPL-2.1+)"

LIC_FILES_CHKSUM = "file://nss/lib/freebl/mpi/doc/LICENSE;md5=491f158d09d948466afce85d6f1fe18f \
                    file://nss/lib/freebl/mpi/doc/LICENSE-MPL;md5=6bf96825e3d7ce4de25621ae886cc859"
SRC_URI = "\
    file://nss-fix-support-cross-compiling.patch \
    file://nss-no-rpath-for-cross-compiling.patch \
    file://nss-fix-incorrect-shebang-of-perl.patch \
    file://nss-3.15.1-fix-CVE-2013-1741.patch \
    file://nss-3.15.1-fix-CVE-2013-5605.patch \
    file://nss-CVE-2014-1492.patch \
    file://nss-CVE-2013-1740.patch \
    file://nss-3.15.1-fix-CVE-2013-1739.patch \
    file://nss-CVE-2013-5606.patch \
    file://nss-CVE-2014-1544.patch \
    file://nss-CVE-2014-1568.patch \
"
SRC_URI_append = "\
    file://nss.pc.in \
    file://signlibs.sh \
"
inherit siteinfo
PR = "r0"
DEPENDS = "sqlite3 nspr zlib nss-native"
DEPENDS_class-native = "sqlite3-native nspr-native zlib-native"
RDEPENDS_${PN} = "perl"

TD = "${S}/tentative-dist"
TDS = "${S}/tentative-dist-staging"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile_prepend_class-native() {
    export NSPR_INCLUDE_DIR=${STAGING_INCDIR_NATIVE}
    export NSPR_LIB_DIR=${STAGING_LIBDIR_NATIVE}
}

do_compile_prepend_class-nativesdk() {
    export LDFLAGS=""
}

do_compile() {
    export CROSS_COMPILE=1
    export NATIVE_CC="gcc"
    export BUILD_OPT=1

    export FREEBL_NO_DEPEND=1
    export FREEBL_LOWHASH=1

    export LIBDIR=${libdir}
    export MOZILLA_CLIENT=1
    export NS_USE_GCC=1
    export NSS_USE_SYSTEM_SQLITE=1
    export NSS_ENABLE_ECC=1

    export OS_RELEASE=3.4
    export OS_TARGET=Linux
    export OS_ARCH=Linux

    if [ "${TARGET_ARCH}" = "powerpc" ]; then
        OS_TEST=ppc
    elif [ "${TARGET_ARCH}" = "powerpc64" ]; then
        OS_TEST=ppc64
    elif [ "${TARGET_ARCH}" = "mips" -o "${TARGET_ARCH}" = "mipsel" -o "${TARGET_ARCH}" = "mips64" -o "${TARGET_ARCH}" = "mips64el" ]; then
        OS_TEST=mips
    else
        OS_TEST="${TARGET_ARCH}"
    fi

    if [ "${SITEINFO_BITS}" = "64" ]; then
        export USE_64=1
    fi

    make -C ./nss CCC="${CXX}" \
        OS_TEST=${OS_TEST} \
}


do_install_prepend_class-nativesdk() {
    export LDFLAGS=""
}

do_install() {
    export CROSS_COMPILE=1
    export NATIVE_CC="gcc"
    export BUILD_OPT=1

    export FREEBL_NO_DEPEND=1

    export LIBDIR=${libdir}
    export MOZILLA_CLIENT=1
    export NS_USE_GCC=1
    export NSS_USE_SYSTEM_SQLITE=1
    export NSS_ENABLE_ECC=1

    export OS_RELEASE=3.4
    export OS_TARGET=Linux
    export OS_ARCH=Linux

    if [ "${TARGET_ARCH}" = "powerpc" ]; then
        OS_TEST=ppc
    elif [ "${TARGET_ARCH}" = "powerpc64" ]; then
        OS_TEST=ppc64
    elif [ "${TARGET_ARCH}" = "mips" -o "${TARGET_ARCH}" = "mipsel" -o "${TARGET_ARCH}" = "mips64" -o "${TARGET_ARCH}" = "mips64el" ]; then
        OS_TEST=mips
    else
        OS_TEST="${TARGET_ARCH}"
    fi
    if [ "${SITEINFO_BITS}" = "64" ]; then
        export USE_64=1
    fi

    make -C ./nss \
        CCC="${CXX}" \
        OS_TEST=${OS_TEST} \
        SOURCE_LIB_DIR="${TD}/${libdir}" \
        SOURCE_BIN_DIR="${TD}/${bindir}" \
        install

    install -d ${D}/${libdir}/
    for file in ${S}/dist/*.OBJ/lib/*.so; do
        echo "Installing `basename $file`..."
        cp $file  ${D}/${libdir}/
    done

    for shared_lib in ${TD}/${libdir}/*.so.*; do
        if [ -f $shared_lib ]; then
            cp $shared_lib ${D}/${libdir}
            ln -sf $(basename $shared_lib) ${D}/${libdir}/$(basename $shared_lib .1oe)
        fi
    done
    for shared_lib in ${TD}/${libdir}/*.so; do
        if [ -f $shared_lib -a ! -e ${D}/${libdir}/$shared_lib ]; then
            cp $shared_lib ${D}/${libdir}
        fi
    done

    install -d ${D}/${includedir}/nss3
    install -m 644 -t ${D}/${includedir}/nss3 dist/public/nss/*

    install -d ${D}/${bindir}
    for binary in ${TD}/${bindir}/*; do
        install -m 755 -t ${D}/${bindir} $binary
    done
}

do_install_append() {
    # Create empty .chk files for the NSS libraries at build time. They could
    # be regenerated at target's boot time.
    for file in libsoftokn3.chk libfreebl3.chk libnssdbm3.chk; do
        touch ${D}/${libdir}/$file
        chmod 755 ${D}/${libdir}/$file
    done
    install -D -m 755 ${WORKDIR}/signlibs.sh ${D}/${bindir}/signlibs.sh

    install -d ${D}${libdir}/pkgconfig/
    sed 's/%NSS_VERSION%/${PV}/' ${WORKDIR}/nss.pc.in | sed 's/%NSPR_VERSION%/4.9.2/' > ${D}${libdir}/pkgconfig/nss.pc
    sed -i s:OEPREFIX:${prefix}:g ${D}${libdir}/pkgconfig/nss.pc
    sed -i s:OEEXECPREFIX:${exec_prefix}:g ${D}${libdir}/pkgconfig/nss.pc
    sed -i s:OELIBDIR:${libdir}:g ${D}${libdir}/pkgconfig/nss.pc
    sed -i s:OEINCDIR:${includedir}/nss3:g ${D}${libdir}/pkgconfig/nss.pc
}

do_install_append_class-target() {
    # Create a blank certificate
    mkdir -p ${D}${sysconfdir}/pki/nssdb/
    touch ./empty_password
    certutil -N -d ${D}${sysconfdir}/pki/nssdb/ -f ./empty_password
    chmod 644 ${D}${sysconfdir}/pki/nssdb/*.db
    rm ./empty_password
}

pkg_postinst_${PN} () {
    if [ -n "$D" ]; then
        for I in $D/${libdir}/lib*.chk; do
            DN=`dirname $I`
            BN=`basename $I .chk`
            FN=$DN/$BN.so
            shlibsign -i $FN
	    if [ $? -ne 0 ]; then
	       exit 1
	    fi
        done
        exit 0
    fi
    signlibs.sh
}

FILES_${PN} = "\
    ${sysconfdir} \
    ${bindir} \
    ${libdir}/lib*.chk \
    ${libdir}/lib*.so \
    "
FILES_${PN}-dev = "\
    ${libdir}/nss \
    ${libdir}/pkgconfig/* \
    ${includedir}/* \
    "
FILES_${PN}-dbg = "\
    ${bindir}/.debug/* \
    ${libdir}/.debug/* \
    "

BBCLASSEXTEND = "native nativesdk"
