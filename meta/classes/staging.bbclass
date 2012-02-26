
packagedstaging_fastpath () {
	:
}

sysroot_stage_dir() {
	src="$1"
	dest="$2"
	# if the src doesn't exist don't do anything
	if [ ! -d "$src" ]; then
		 return
	fi

	# We only want to stage the contents of $src if it's non-empty so first rmdir $src
	# then if it still exists (rmdir on non-empty dir fails) we can copy its contents
	rmdir "$src" 2> /dev/null || true
	# However we always want to stage a $src itself, even if it's empty
	mkdir -p "$dest"
	if [ -d "$src" ]; then
		tar -cf - -C "$src" -ps . | tar -xf - -C "$dest"
	fi
}

sysroot_stage_libdir() {
	src="$1"
	dest="$2"

	sysroot_stage_dir $src $dest
}

sysroot_stage_dirs() {
	from="$1"
	to="$2"

	sysroot_stage_dir $from${includedir} $to${includedir}
	if [ "${BUILD_SYS}" = "${HOST_SYS}" ]; then
		sysroot_stage_dir $from${bindir} $to${bindir}
		sysroot_stage_dir $from${sbindir} $to${sbindir}
		sysroot_stage_dir $from${base_bindir} $to${base_bindir}
		sysroot_stage_dir $from${base_sbindir} $to${base_sbindir}
		sysroot_stage_dir $from${libexecdir} $to${libexecdir}
		sysroot_stage_dir $from${sysconfdir} $to${sysconfdir}
		sysroot_stage_dir $from${localstatedir} $to${localstatedir}
	fi
	if [ -d $from${libdir} ]
	then
		sysroot_stage_libdir $from/${libdir} $to${libdir}
	fi
	if [ -d $from${base_libdir} ]
	then
		sysroot_stage_libdir $from${base_libdir} $to${base_libdir}
	fi
	sysroot_stage_dir $from${datadir} $to${datadir}
}

sysroot_stage_all() {
	sysroot_stage_dirs ${D} ${SYSROOT_DESTDIR}
}

do_populate_sysroot[dirs] = "${SYSROOT_DESTDIR}"
do_populate_sysroot[umask] = "022"

addtask populate_sysroot after do_install

SYSROOT_PREPROCESS_FUNCS ?= ""
SYSROOT_DESTDIR = "${WORKDIR}/sysroot-destdir/"
SYSROOT_LOCK = "${STAGING_DIR}/staging.lock"

# We clean out any existing sstate from the sysroot if we rerun configure
python sysroot_cleansstate () {
     ss = sstate_state_fromvars(d, "populate_sysroot")
     sstate_clean(ss, d)
}
do_configure[prefuncs] += "sysroot_cleansstate"


BB_SETSCENE_VERIFY_FUNCTION = "sysroot_checkhashes"

def sysroot_checkhashes(covered, tasknames, fnids, fns, d):
    problems = set()
    configurefnids = set()
    for task in xrange(len(tasknames)):
        if tasknames[task] == "do_configure" and task not in covered:
            configurefnids.add(fnids[task])
    for task in covered:
        if tasknames[task] == "do_populate_sysroot" and fnids[task] in configurefnids:
            problems.add(task)
    return problems

python do_populate_sysroot () {
    #
    # if do_stage exists, we're legacy. In that case run the do_stage,
    # modify the SYSROOT_DESTDIR variable and then run the staging preprocess
    # functions against staging directly.
    #
    # Otherwise setup a destdir, copy the results from do_install
    # and run the staging preprocess against that
    #

    bb.build.exec_func("sysroot_stage_all", d)
    for f in (d.getVar('SYSROOT_PREPROCESS_FUNCS', True) or '').split():
        bb.build.exec_func(f, d)
}

SSTATETASKS += "do_populate_sysroot"
do_populate_sysroot[sstate-name] = "populate-sysroot"
do_populate_sysroot[sstate-inputdirs] = "${SYSROOT_DESTDIR}"
do_populate_sysroot[sstate-outputdirs] = "${STAGING_DIR_HOST}/"
do_populate_sysroot[stamp-extra-info] = "${MACHINE}"

python do_populate_sysroot_setscene () {
	sstate_setscene(d)
}
addtask do_populate_sysroot_setscene

python () {
    if d.getVar('do_stage', True) is not None:
        bb.fatal("Legacy staging found for %s as it has a do_stage function. This will need conversion to a do_install or often simply removal to work with OE-core" % d.getVar("FILE", True))
}


