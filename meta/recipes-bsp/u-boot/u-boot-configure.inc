# This provides the logic for creating the desired u-boot config,
# accounting for any *.cfg files added to SRC_URI.  It's separated
# from u-boot.inc for use by recipes that need u-boot properly
# configured but aren't doing a full build of u-boot itself (such as
# its companion tools).

inherit uboot-config cml1

DEPENDS += "kern-tools-native"

CONFIGURE_FILES = "${@d.getVar('UBOOT_MACHINE') or '.config'}"

do_configure () {
    if [ -n "${UBOOT_CONFIG}" ]; then
        unset i j
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    uboot_configure_config $config $type
                fi
            done
            unset j
        done
        unset i
    else
        uboot_configure
    fi
}

uboot_configure_config () {
    config=$1
    type=$2

    unset k
    IFS="?"
    uboot_config_make_opts="${UBOOT_CONFIG_MAKE_OPTS}"
    for config_make_opts in $uboot_config_make_opts; do
        k=$(expr $k + 1);
        if [ $k -eq $i ]; then
            break
        fi
    done
    unset IFS
    unset k

    oe_runmake -C ${S} O=${B}/${config} ${config_make_opts} ${UBOOT_MAKE_OPTS} ${config}
    if [ -n "${@' '.join(find_cfgs(d))}" ]; then
        merge_config.sh -m -O ${B}/${config} ${B}/${config}/.config ${@" ".join(find_cfgs(d))}
        oe_runmake -C ${S} O=${B}/${config} oldconfig
    fi
}

uboot_configure () {
    if [ -n "${UBOOT_MACHINE}" ]; then
        oe_runmake -C ${S} O=${B} ${UBOOT_MAKE_OPTS} ${UBOOT_MACHINE}
    else
        oe_runmake -C ${S} O=${B} ${UBOOT_MAKE_OPTS} oldconfig
    fi
    merge_config.sh -m .config ${@" ".join(find_cfgs(d))}
    cml1_do_configure
}

