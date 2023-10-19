# Checks related to the patch's CVE lines
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: GPL-2.0-only
#

import base
import pyparsing

class CVE(base.Base):

    re_cve_pattern = pyparsing.Regex("CVE\-\d{4}\-\d+")
    re_cve_payload_tag = pyparsing.Regex("\+CVE:(\s+CVE\-\d{4}\-\d+)+")

    def setUp(self):
        if self.unidiff_parse_error:
            self.skip('Parse error %s' % self.unidiff_parse_error)

        # we are just interested in series that introduce CVE patches, thus discard other
        # possibilities: modification to current CVEs, patch directly introduced into the
        # recipe, upgrades already including the CVE, etc.
        new_cves = [p for p in self.patchset if p.path.endswith('.patch') and p.is_added_file]
        if not new_cves:
            self.skip('No new CVE patches introduced')

    def test_cve_tag_format(self):
        for commit in CVE.commits:
            if self.re_cve_pattern.search_string(commit.shortlog) or self.re_cve_pattern.search_string(commit.commit_message):
                tag_found = False
                for line in commit.payload.splitlines():
                    if self.re_cve_payload_tag.search_string(line):
                        tag_found = True
                        break
                if not tag_found:
                    self.fail('Missing or incorrectly formatted CVE tag in patch file. Correct or include the CVE tag in the patch with format: "CVE: CVE-YYYY-XXXX"',
                              commit=commit)
