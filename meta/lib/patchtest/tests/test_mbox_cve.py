# Checks related to the patch's CVE lines
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: GPL-2.0-only
#

import base
import parse_cve_tags
import pyparsing

class CVE(base.Base):

    revert_shortlog_regex = pyparsing.Regex('Revert\s+".*"')
    prog = parse_cve_tags.cve_tag
    patch_prog = parse_cve_tags.patch_cve_tag

    def setUp(self):
        if self.unidiff_parse_error:
            self.skip('Parse error %s' % self.unidiff_parse_error)

        # we are just interested in series that introduce CVE patches, thus discard other
        # possibilities: modification to current CVEs, patch directly introduced into the
        # recipe, upgrades already including the CVE, etc.
        new_patches = [p for p in self.patchset if p.path.endswith('.patch') and p.is_added_file]
        if not new_patches:
            self.skip('No new patches introduced')

    def test_cve_presence_in_commit_message(self):
        for commit in CVE.commits:
            # skip those patches that revert older commits, these do not required the tag presence
            if self.revert_shortlog_regex.search_string(commit.shortlog):
                continue
            if not self.patch_prog.search_string(commit.payload):
                self.skip("No CVE tag in added patch, so not needed in mbox")
            elif not self.prog.search_string(commit.payload):
                self.fail('Missing or incorrectly formatted CVE tag in mbox. Correct or include the CVE tag in the mbox with format: "CVE: CVE-YYYY-XXXX"',
                          commit=commit)
