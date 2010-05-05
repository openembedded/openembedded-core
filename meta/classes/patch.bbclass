# Copyright (C) 2006  OpenedHand LTD

# Point to an empty file so any user's custom settings don't break things
QUILTRCFILE ?= "${STAGING_BINDIR_NATIVE}/quiltrc"

PATCHDEPENDENCY = "${PATCHTOOL}-native:do_populate_sysroot"

python patch_do_patch() {
        import oe.patch

	src_uri = (bb.data.getVar('SRC_URI', d, 1) or '').split()
	if not src_uri:
		return

	patchsetmap = {
		"patch": oe.patch.PatchTree,
		"quilt": oe.patch.QuiltTree,
		"git": oe.patch.GitApplyTree,
	}

	cls = patchsetmap[bb.data.getVar('PATCHTOOL', d, 1) or 'quilt']

	resolvermap = {
		"noop": oe.patch.NOOPResolver,
		"user": oe.patch.UserResolver,
	}

	rcls = resolvermap[bb.data.getVar('PATCHRESOLVE', d, 1) or 'noop']

	s = bb.data.getVar('S', d, 1)

	path = os.getenv('PATH')
	os.putenv('PATH', bb.data.getVar('PATH', d, 1))
	patchset = cls(s, d)
	patchset.Clean()

	resolver = rcls(patchset)

	workdir = bb.data.getVar('WORKDIR', d, 1)
	for url in src_uri:
		(type, host, path, user, pswd, parm) = bb.decodeurl(url)
		if not "patch" in parm:
			continue

		bb.fetch.init([url],d)
		url = bb.encodeurl((type, host, path, user, pswd, []))
		local = os.path.join('/', bb.fetch.localpath(url, d))

		# did it need to be unpacked?
		dots = os.path.basename(local).split(".")
		if dots[-1] in ['gz', 'bz2', 'Z']:
			unpacked = os.path.join(bb.data.getVar('WORKDIR', d),'.'.join(dots[0:-1]))
		else:
			unpacked = local
		unpacked = bb.data.expand(unpacked, d)

		if "pnum" in parm:
			pnum = parm["pnum"]
		else:
			pnum = "1"

		if "pname" in parm:
			pname = parm["pname"]
		else:
			pname = os.path.basename(unpacked)

                if "mindate" in parm or "maxdate" in parm:
			pn = bb.data.getVar('PN', d, 1)
			srcdate = bb.data.getVar('SRCDATE_%s' % pn, d, 1)
			if not srcdate:
				srcdate = bb.data.getVar('SRCDATE', d, 1)

			if srcdate == "now":
				srcdate = bb.data.getVar('DATE', d, 1)

			if "maxdate" in parm and parm["maxdate"] < srcdate:
				bb.note("Patch '%s' is outdated" % pname)
				continue

			if "mindate" in parm and parm["mindate"] > srcdate:
				bb.note("Patch '%s' is predated" % pname)
				continue


		if "minrev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)
			if srcrev and srcrev < parm["minrev"]:
				bb.note("Patch '%s' applies to later revisions" % pname)
				continue

		if "maxrev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)		
			if srcrev and srcrev > parm["maxrev"]:
				bb.note("Patch '%s' applies to earlier revisions" % pname)
				continue

		if "rev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)		
			if srcrev and parm["rev"] not in srcrev:
				bb.note("Patch '%s' doesn't apply to revision" % pname)
				continue

		if "notrev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)		
			if srcrev and parm["notrev"] in srcrev:
				bb.note("Patch '%s' doesn't apply to revision" % pname)
				continue

		bb.note("Applying patch '%s'" % pname)
		try:
			patchset.Import({"file":unpacked, "remote":url, "strippath": pnum}, True)
			resolver.Resolve()
		except Exception:
			import sys
			raise bb.build.FuncFailed(str(sys.exc_value))
}

addtask patch after do_unpack
do_patch[dirs] = "${WORKDIR}"
do_patch[depends] = "${PATCHDEPENDENCY}"
EXPORT_FUNCTIONS do_patch
