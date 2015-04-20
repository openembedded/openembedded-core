SUMMARY = "Universal Boot Loader for embedded devices"
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
PROVIDES = "virtual/bootloader"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=c7383a594871c03da76b3707929d2919"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit uboot-config deploy

EXTRA_OEMAKE = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${TARGET_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" V=1'

# Allow setting an additional version string that will be picked up by the
# u-boot build system and appended to the u-boot version.  If the .scmversion
# file already exists it will not be overwritten.
UBOOT_LOCALVERSION ?= ""

# Some versions of u-boot use .bin and others use .img.  By default use .bin
# but enable individual recipes to change this value.
UBOOT_SUFFIX ??= "bin"
UBOOT_IMAGE ?= "u-boot-${MACHINE}-${PV}-${PR}.${UBOOT_SUFFIX}"
UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"
UBOOT_SYMLINK ?= "u-boot-${MACHINE}.${UBOOT_SUFFIX}"
UBOOT_MAKE_TARGET ?= "all"

# Some versions of u-boot build an SPL (Second Program Loader) image that
# should be packaged along with the u-boot binary as well as placed in the
# deploy directory.  For those versions they can set the following variables
# to allow packaging the SPL.
SPL_BINARY ?= ""
SPL_IMAGE ?= "${SPL_BINARY}-${MACHINE}-${PV}-${PR}"
SPL_SYMLINK ?= "${SPL_BINARY}-${MACHINE}"

# Additional environment variables or a script can be installed alongside
# u-boot to be used automatically on boot.  This file, typically 'uEnv.txt'
# or 'boot.scr', should be packaged along with u-boot as well as placed in the
# deploy directory.  Machine configurations needing one of these files should
# include it in the SRC_URI and set the UBOOT_ENV parameter.
UBOOT_ENV_SUFFIX ?= "txt"
UBOOT_ENV ?= ""
UBOOT_ENV_BINARY ?= "${UBOOT_ENV}.${UBOOT_ENV_SUFFIX}"
UBOOT_ENV_IMAGE ?= "${UBOOT_ENV}-${MACHINE}-${PV}-${PR}.${UBOOT_ENV_SUFFIX}"
UBOOT_ENV_SYMLINK ?= "${UBOOT_ENV}-${MACHINE}.${UBOOT_ENV_SUFFIX}"

do_compile () {
	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', 'ld-is-gold', '', d)}" = "ld-is-gold" ] ; then
		sed -i 's/$(CROSS_COMPILE)ld$/$(CROSS_COMPILE)ld.bfd/g' config.mk
	fi

	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS

	if [ ! -e ${B}/.scmversion -a ! -e ${S}/.scmversion ]
	then
		echo ${UBOOT_LOCALVERSION} > ${B}/.scmversion
		echo ${UBOOT_LOCALVERSION} > ${S}/.scmversion
	fi
    
    if [ "x${UBOOT_CONFIG}" != "x" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=`expr $i + 1`;
            for type  in ${UBOOT_CONFIG}; do
                j=`expr $j + 1`;
                if [ $j -eq $i ]
                then
                    oe_runmake O=${config} ${config}
                    oe_runmake O=${config} ${UBOOT_MAKE_TARGET}
                    cp  ${S}/${config}/${UBOOT_BINARY}  ${S}/${config}/u-boot-${type}.${UBOOT_SUFFIX}
                fi
            done
            unset  j
        done
        unset  i
    else
        oe_runmake ${UBOOT_MACHINE}
        oe_runmake ${UBOOT_MAKE_TARGET}
    fi

}

do_install () {
    if [ "x${UBOOT_CONFIG}" != "x" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=`expr $i + 1`;
            for type in ${UBOOT_CONFIG}; do
                j=`expr $j + 1`;
                if [ $j -eq $i ]
                then
                    install -d ${D}/boot
                    install ${S}/${config}/u-boot-${type}.${UBOOT_SUFFIX} ${D}/boot/u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX}
                    ln -sf u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${D}/boot/${UBOOT_BINARY}
                fi
            done
            unset  j
        done
        unset  i
    else
        install -d ${D}/boot
        install ${S}/${UBOOT_BINARY} ${D}/boot/${UBOOT_IMAGE}
        ln -sf ${UBOOT_IMAGE} ${D}/boot/${UBOOT_BINARY}
    fi

    if [ -e ${WORKDIR}/fw_env.config ] ; then
        install -d ${D}${sysconfdir}
        install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
    fi

    if [ "x${SPL_BINARY}" != "x" ]
    then
        if [ "x${UBOOT_CONFIG}" != "x" ]   
        then
            for config in ${UBOOT_MACHINE}; do
                i=`expr $i + 1`;
                for type in ${UBOOT_CONFIG}; do
                    j=`expr $j + 1`;
                    if [ $j -eq $i ]
                    then
                         install ${S}/${config}/${SPL_BINARY} ${D}/boot/${SPL_IMAGE}-${type}-${PV}-${PR}
                         ln -sf ${SPL_IMAGE}-${type}-${PV}-${PR} ${D}/boot/${SPL_BINARY}-${type}
                         ln -sf ${SPL_IMAGE}-${type}-${PV}-${PR} ${D}/boot/${SPL_BINARY}
                    fi
                done
                unset  j
            done
            unset  i
        else
            install ${S}/${SPL_BINARY} ${D}/boot/${SPL_IMAGE}
            ln -sf ${SPL_IMAGE} ${D}/boot/${SPL_BINARY}
        fi
    fi

    if [ "x${UBOOT_ENV}" != "x" ]
    then
        install ${WORKDIR}/${UBOOT_ENV_BINARY} ${D}/boot/${UBOOT_ENV_IMAGE}
        ln -sf ${UBOOT_ENV_IMAGE} ${D}/boot/${UBOOT_ENV_BINARY}
    fi
}

FILES_${PN} = "/boot ${sysconfdir}"

do_deploy () {
    if [ "x${UBOOT_CONFIG}" != "x" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=`expr $i + 1`;
            for type in ${UBOOT_CONFIG}; do
                j=`expr $j + 1`;
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}
                    install ${S}/${config}/u-boot-${type}.${UBOOT_SUFFIX} ${DEPLOYDIR}/u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX}
                    cd ${DEPLOYDIR}
                    ln -sf u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${UBOOT_SYMLINK}
                    ln -sf u-boot-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${UBOOT_BINARY}
                fi 
            done 
            unset  j
        done
        unset  i
    else
        install -d ${DEPLOYDIR}
        install ${S}/${UBOOT_BINARY} ${DEPLOYDIR}/${UBOOT_IMAGE}
        cd ${DEPLOYDIR}
        rm -f ${UBOOT_BINARY} ${UBOOT_SYMLINK}
        ln -sf ${UBOOT_IMAGE} ${UBOOT_SYMLINK}
        ln -sf ${UBOOT_IMAGE} ${UBOOT_BINARY}
   fi
    


     if [ "x${SPL_BINARY}" != "x" ]
     then
         if [ "x${UBOOT_CONFIG}" != "x" ]   
         then
             for config in ${UBOOT_MACHINE}; do
                 i=`expr $i + 1`;
                 for type in ${UBOOT_CONFIG}; do
                     j=`expr $j + 1`;
                     if [ $j -eq $i ]
                     then
                         install ${S}/${config}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_IMAGE}-${type}-${PV}-${PR}
                         rm -f ${DEPLOYDIR}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_SYMLINK}-${type}
                         ln -sf ${SPL_IMAGE}-${type}-${PV}-${PR} ${DEPLOYDIR}/${SPL_BINARY}-${type}
                         ln -sf ${SPL_IMAGE}-${type}-${PV}-${PR} ${DEPLOYDIR}/${SPL_BINARY}
                         ln -sf ${SPL_IMAGE}-${type}-${PV}-${PR} ${DEPLOYDIR}/${SPL_SYMLINK}-${type}
                         ln -sf ${SPL_IMAGE}-${type}-${PV}-${PR} ${DEPLOYDIR}/${SPL_SYMLINK}
                     fi
                 done
                 unset  j
             done
             unset  i
         else
             install ${S}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_IMAGE}
             rm -f ${DEPLOYDIR}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_SYMLINK}
             ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_BINARY}
             ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_SYMLINK}
         fi
     fi


    if [ "x${UBOOT_ENV}" != "x" ]
    then
        install ${WORKDIR}/${UBOOT_ENV_BINARY} ${DEPLOYDIR}/${UBOOT_ENV_IMAGE}
        rm -f ${DEPLOYDIR}/${UBOOT_ENV_BINARY} ${DEPLOYDIR}/${UBOOT_ENV_SYMLINK}
        ln -sf ${UBOOT_ENV_IMAGE} ${DEPLOYDIR}/${UBOOT_ENV_BINARY}
        ln -sf ${UBOOT_ENV_IMAGE} ${DEPLOYDIR}/${UBOOT_ENV_SYMLINK}
    fi
}

addtask deploy before do_build after do_compile
