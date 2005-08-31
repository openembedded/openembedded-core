inherit qmake-base

qmake_do_configure() {
	case ${QMAKESPEC} in
	*linux-oe-g++|*linux-uclibc-oe-g++)
		;;
	*-oe-g++)
		die Unsupported target ${TARGET_OS} for oe-g++ qmake spec
		;;
	*)
		oenote Searching for qmake spec file
		paths="${QMAKE_MKSPEC_PATH}/qws/${TARGET_OS}-${TARGET_ARCH}-g++"
		paths="${QMAKE_MKSPEC_PATH}/${TARGET_OS}-g++ $paths"

		if (echo "${TARGET_ARCH}"|grep -q 'i.86'); then
			paths="${QMAKE_MKSPEC_PATH}/qws/${TARGET_OS}-x86-g++ $paths"
		fi
		for i in $paths; do
			if test -e $i; then
				export QMAKESPEC=$i
				break
			fi
		done
		;;
	esac

	oenote "using qmake spec in ${QMAKESPEC}, using profiles '${QMAKE_PROFILES}'"

	if [ -z "${QMAKE_PROFILES}" ]; then 
		PROFILES="`ls *.pro`"
	else
		PROFILES="${QMAKE_PROFILES}"
	fi

	if [ -z "$PROFILES" ]; then
		die "QMAKE_PROFILES not set and no profiles found in $PWD"
        fi

	if [ ! -z "${EXTRA_QMAKEVARS_POST}" ]; then
		AFTER="-after"
		QMAKE_VARSUBST_POST="${EXTRA_QMAKEVARS_POST}"
		oenote "qmake postvar substitution: ${EXTRA_QMAKEVARS_POST}"
	fi

	if [ ! -z "${EXTRA_QMAKEVARS_PRE}" ]; then
		QMAKE_VARSUBST_PRE="${EXTRA_QMAKEVARS_PRE}"
		oenote "qmake prevar substitution: ${EXTRA_QMAKEVARS_PRE}"
	fi

#oenote "Calling 'qmake -makefile -spec ${QMAKESPEC} -o Makefile $QMAKE_VARSUBST_PRE $AFTER $PROFILES $QMAKE_VARSUBST_POST'"
	unset QMAKESPEC || true
	qmake -makefile -spec ${QMAKESPEC} -o Makefile $QMAKE_VARSUBST_PRE $AFTER $PROFILES $QMAKE_VARSUBST_POST || die "Error calling qmake on $PROFILES"
}

EXPORT_FUNCTIONS do_configure

addtask configure after do_unpack do_patch before do_compile
