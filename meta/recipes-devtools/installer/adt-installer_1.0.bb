# Yocto ADT Installer bb file
#
# Copyright 2010-2011 by Intel Corp.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy 
# of this software and associated documentation files (the "Software"), to deal 
# in the Software without restriction, including without limitation the rights 
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
# copies of the Software, and to permit persons to whom the Software is 
# furnished to do so, subject to the following conditions:

# The above copyright notice and this permission notice shall be included in 
# all copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
# THE SOFTWARE.


DESCRIPTION = "Meta package for creating sdk installer tarball"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
LICENSE = "MIT"

ALLOW_EMPTY = "1"

PACKAGES = ""
PACKAGE_ARCH = "all"

PR = "r4"

ADT_DEPLOY = "${TMPDIR}/deploy/sdk/"
ADT_DIR = "${WORKDIR}/adt-installer/"
YOCTOADT_VERSION = "${SDK_VERSION}"
S = "${WORKDIR}/trunk"

inherit deploy

SRCREV = "596"
PV = "0.1.8+svnr${SRCPV}"
SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;proto=http \
           file://wget_cache.patch \
           file://adt_installer \
           file://scripts/adt_installer_internal \
           file://scripts/util \
           file://scripts/data_define \
           file://scripts/extract_rootfs \
           file://adt_installer.conf \
           file://opkg/conf/opkg-sdk-x86_64.conf \
           file://opkg/conf/opkg-sdk-i686.conf \
	  "

ADTREPO = "http://adtrepo.yoctoproject.org/${SDK_VERSION}"

do_deploy[umask] = 022

fakeroot do_deploy () {
	cd ${WORKDIR}
	mkdir -p ${ADT_DEPLOY}
	rm -f ${ADT_DEPLOY}/adt-installer.tar.bz2
	rm -rf ${ADT_DIR}
	mkdir -p ${ADT_DIR}/opkg/build
	cp -r opkg ${ADT_DIR}/
	sed -i -e 's#ADTREPO_URL#${ADTREPO}#' ${ADT_DIR}/opkg/conf/*.conf
	cp -r trunk ${ADT_DIR}/opkg/build/
	mv ${ADT_DIR}/opkg/build/trunk ${ADT_DIR}/opkg/build/opkg-svn
	cp -r scripts ${ADT_DIR}/
	cp adt_installer ${ADT_DIR}
	cp adt_installer.conf ${ADT_DIR}
	echo 'YOCTOADT_VERSION=${SDK_VERSION}' > ${ADT_DIR}/temp.conf
        cat ${ADT_DIR}/adt_installer.conf >> ${ADT_DIR}/temp.conf
        mv ${ADT_DIR}/temp.conf ${ADT_DIR}/adt_installer.conf
        echo 'SDK_VENDOR=${SDK_VENDOR}' >> ${ADT_DIR}/scripts/data_define
        echo 'INSTALL_FOLDER=${SDKPATH}' >> ${ADT_DIR}/scripts/data_define
	tar cfj adt_installer.tar.bz2 adt-installer
	cp ${WORKDIR}/adt_installer.tar.bz2 ${ADT_DEPLOY}
}

do_install[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_package[noexec] = "1"
do_package_write[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_rpm[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_poplulate_sysroot[noexec] = "1"

addtask deploy before do_populate_sysroot after do_patch
