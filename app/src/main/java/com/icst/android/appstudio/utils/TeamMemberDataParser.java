/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.icst.android.appstudio.models.SocialProfile;
import com.icst.android.appstudio.models.TeamMember;

public class TeamMemberDataParser {
	public static ArrayList<TeamMember> getMembers(
			String contributorsData, String contributorsAdditionalData) {
		ArrayList<TeamMember> teamMembers = new ArrayList<TeamMember>();

		try {
			JSONArray members = new JSONArray(contributorsData);
			for (int membersPosition = 0; membersPosition < members.length(); ++membersPosition) {
				JSONObject memberData = members.getJSONObject(membersPosition);
				TeamMember member = new TeamMember();
				member.setName(memberData.getString("login"));
				member.setProfilePhotoUrl(memberData.getString("avatar_url"));
				member.setDescription(memberData.getString("contributions").concat(" commits"));
				member.setId(memberData.getLong("id"));

				SocialProfile gitProfile = new SocialProfile();
				gitProfile.setPlatformName("GitHub");
				gitProfile.setPlatformIconUrl(
						"https://dl.dropbox.com/scl/fi/0xxmle6zxtg3714d04kt3/GitHub.png?rlkey=0jswbu2q9ac4s62z7icyzg5ko&dl=0");
				gitProfile.setUrl("https://github.com/".concat(memberData.getString("login")));
				ArrayList<SocialProfile> profiles = new ArrayList<SocialProfile>();
				profiles.add(gitProfile);
				member.setSocialProfiles(profiles);
				teamMembers.add(member);
			}

			/*
			 * Merge additional properties of Members
			 */
			JSONObject additionalData = new JSONObject(contributorsAdditionalData);
			JSONArray contributorsAdditionalObject = additionalData.getJSONArray("Contributors");
			for (int memberCount = 0; memberCount < contributorsAdditionalObject.length(); ++memberCount) {
				if (!contributorsAdditionalObject.getJSONObject(memberCount).isNull("id")) {

					long id = contributorsAdditionalObject.getJSONObject(memberCount).getLong("id");

					for (int teamMembersCount = 0; teamMembersCount < teamMembers.size(); ++teamMembersCount) {

						TeamMember member = teamMembers.get(teamMembersCount);
						if (member.getId() == id) {

							if (!contributorsAdditionalObject.getJSONObject(memberCount).isNull("Tag")) {
								member.setTag(
										contributorsAdditionalObject.getJSONObject(memberCount).getString("Tag"));
							}

							if (!contributorsAdditionalObject.getJSONObject(memberCount).isNull("Social")) {

								ArrayList<SocialProfile> profiles = member.getSocialProfiles();

								if (profiles == null) {
									profiles = new ArrayList<SocialProfile>();
								}

								for (int socialProfileCount = 0; socialProfileCount < contributorsAdditionalObject
										.getJSONObject(memberCount)
										.getJSONArray("Social")
										.length(); ++socialProfileCount) {
									SocialProfile profile = new SocialProfile();
									if (!contributorsAdditionalObject
											.getJSONObject(memberCount)
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.isNull("platformName")) {
										profile.setPlatformName(
												contributorsAdditionalObject
														.getJSONObject(memberCount)
														.getJSONArray("Social")
														.getJSONObject(socialProfileCount)
														.getString("platformName"));
									}
									if (!contributorsAdditionalObject
											.getJSONObject(memberCount)
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.isNull("url")) {
										profile.setUrl(
												contributorsAdditionalObject
														.getJSONObject(memberCount)
														.getJSONArray("Social")
														.getJSONObject(socialProfileCount)
														.getString("url"));
									}
									if (!contributorsAdditionalObject
											.getJSONObject(memberCount)
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.isNull("platformIconUrl")) {
										profile.setPlatformIconUrl(
												contributorsAdditionalObject
														.getJSONObject(memberCount)
														.getJSONArray("Social")
														.getJSONObject(socialProfileCount)
														.getString("platformIconUrl"));
									}
									profiles.add(profile);
								}
								member.setSocialProfiles(profiles);
							}
						}
					}
				} else {
					JSONObject nonGitContributor = contributorsAdditionalObject.getJSONObject(memberCount);
					TeamMember member = new TeamMember();
					member.setName(nonGitContributor.getString("name"));
					if (!nonGitContributor.isNull("Tag")) {
						member.setTag(nonGitContributor.getString("Tag"));
					}
					member.setProfilePhotoUrl(nonGitContributor.getString("avatar_url"));
					member.setDescription(nonGitContributor.getString("decription"));

					ArrayList<SocialProfile> profiles = new ArrayList<SocialProfile>();
					for (int socialProfileCount = 0; socialProfileCount < nonGitContributor.getJSONArray("Social")
							.length(); ++socialProfileCount) {
						SocialProfile profile = new SocialProfile();
						if (!nonGitContributor
								.getJSONArray("Social")
								.getJSONObject(socialProfileCount)
								.isNull("platformName")) {
							profile.setPlatformName(
									nonGitContributor
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.getString("platformName"));
						}
						if (!nonGitContributor
								.getJSONArray("Social")
								.getJSONObject(socialProfileCount)
								.isNull("url")) {
							profile.setUrl(
									nonGitContributor
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.getString("url"));
						}
						if (!nonGitContributor
								.getJSONArray("Social")
								.getJSONObject(socialProfileCount)
								.isNull("platformIconUrl")) {
							profile.setPlatformIconUrl(
									nonGitContributor
											.getJSONArray("Social")
											.getJSONObject(socialProfileCount)
											.getString("platformIconUrl"));
						}
						profiles.add(profile);
					}
					member.setSocialProfiles(profiles);
					teamMembers.add(member);
				}
			}
		} catch (JSONException e) {
		}
		return teamMembers;
	}
}
